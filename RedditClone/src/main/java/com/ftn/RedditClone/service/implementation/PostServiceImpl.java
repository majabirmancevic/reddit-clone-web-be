package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.elasticRepository.CommunityElasticRepository;
import com.ftn.RedditClone.elasticRepository.PostElasticRepository;
import com.ftn.RedditClone.exceptions.CommunityNotFoundException;
import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.lucene.indexing.handlers.*;
import com.ftn.RedditClone.mapper.PostMapper;
import com.ftn.RedditClone.mapper.elastic.PostESMapper;
import com.ftn.RedditClone.model.entity.*;
import com.ftn.RedditClone.model.entity.dto.*;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import com.ftn.RedditClone.repository.*;
import com.ftn.RedditClone.service.PostService;
import com.ftn.RedditClone.service.SearchQueryGenerator;
import com.ftn.RedditClone.service.UserService;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

//    @Value("${files.path}")
//    private final String filesPath;
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final ReactionRepository reactionRepository;
    private final FlairRepository flairRepository;

    private final PostElasticRepository postElasticRepository;

    private final CommunityElasticRepository communityElasticRepository;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    ElasticsearchOperations operations;

   // private final PostESMapper postESMapper;

    @Override
    public Post save(PostRequest postRequest) throws IOException {
        Community community = communityRepository.findByName(postRequest.getCommunityName());


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        Post post = new Post();

        if (postRequest.getFlair() != null) {
            Flair flair = new Flair();
            flair = flairRepository.findByName(postRequest.getFlair());
            post = postMapper.map(postRequest, community, user, flair);
        }
        else{
            post = postMapper.map(postRequest, community, user, null);
        }

        //dodato
        Reaction reaction = new Reaction();
        reaction.setType(ReactionType.UPVOTE);
        reaction.setPost(post);
        reaction.setUser(user);
        reaction.setComment(null);
        reaction.setTimestamp(LocalDate.now());


        community.addPost(post);
        post = postRepository.save(post);
        reactionRepository.save(reaction);

        indexUploadedFile(postRequest);


        return post;

    }

    public void indexUploadedFile(PostRequest dto) throws IOException {

        CommunityElastic communityElastic = communityElasticRepository.findByName(dto.getCommunityName());
        if(communityElastic.getPosts() == null) {
            List<PostElastic> posts = new ArrayList<PostElastic>();
            communityElastic.setPosts(posts);
        }
        if (dto.getFiles() == null){
            PostElastic postIndexUnit = PostESMapper.mapDtoToPost(dto);
            index(postIndexUnit);

            int posts = postElasticRepository.findAllByCommunityName(communityElastic.getName()).size() ;
            communityElastic.setNumOfPosts(posts);
            communityElastic.getPosts().add(postIndexUnit);
            communityElasticRepository.save(communityElastic);
        }else{
            for (MultipartFile file : dto.getFiles()) {
                if (file.isEmpty()) {
                    continue;
                }

                String fileName = saveUploadedFileInFolder(file);
                if(fileName != null){
                    PostElastic postIndexUnit = getHandler(fileName).getIndexUnitPost(new File(fileName));
                    postIndexUnit.setName(dto.getPostName());
                    postIndexUnit.setDescription(dto.getText());
                    postIndexUnit.setReactionCount(1);
                    postIndexUnit.setCommunityName(dto.getCommunityName());
                    index(postIndexUnit);

                    int posts = postElasticRepository.findAllByCommunityName(communityElastic.getName()).size();
                    communityElastic.setNumOfPosts(posts);
                    communityElastic.getPosts().add(postIndexUnit);
                    communityElasticRepository.save(communityElastic);
                    //operations.refresh(CommunityElastic.class);
                }
            }
        }
    }

    private List<PostResponseElastic> mapPostsToDTO(List<PostElastic> posts){
        List<PostResponseElastic> postsDTO = new ArrayList<>();
        for(PostElastic post : posts){
            postsDTO.add(PostESMapper.postToResponse(post));
        }
        return postsDTO;
    }

    public void index(PostElastic postElastic) {
        postElasticRepository.save(postElastic);
    }
    public DocumentHandler getHandler(String fileName){
        return getDocumentHandler(fileName);
    }

    public static DocumentHandler getDocumentHandler(String fileName) {
        if(fileName.endsWith(".txt")){
            return new TextDocHandler();
        }else if(fileName.endsWith(".pdf")){
            return new PDFHandler();
        }else if(fileName.endsWith(".doc")){
            return new WordHandler();
        }else if(fileName.endsWith(".docx")){
            return new Word2007Handler();
        }else{
            return null;
        }
    }

    private String saveUploadedFileInFolder(MultipartFile file) throws IOException {
        String retVal = null;
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(new File("RedditClone\\src\\main\\resources\\files\\").getAbsolutePath() + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }

    public void reindex(){
        postElasticRepository.deleteAll();
        //elasticsearchRestTemplate.indexOps(IndexCoordinates.of("communities")).delete();
        elasticsearchRestTemplate.indexOps(PostElastic.class).delete();
        elasticsearchRestTemplate.indexOps(PostElastic.class).create();
    }

    @Override
    public Post save(Post post){
        return postRepository.save(post);
    }

    @Override
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Override
    public Post findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return post;
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public List<PostResponse> getPostsByCommunity(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException(communityId.toString()));
        List<Post> posts = postRepository.findAllByCommunity(community);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public boolean removePost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        if(post == null){
            return false;
        } else {
            PostElastic postElastic = postElasticRepository.findByName(post.getTitle());
            postRepository.deleteCurrent(id);
            post.setCommunity(null);

            postElasticRepository.delete(postElastic);
            CommunityElastic communityElastic = communityElasticRepository.findByName(postElastic.getCommunityName());
            communityElastic.getPosts().remove(postElastic);

            return true;
        }
    }

    @Override
    public List<PostResponseElastic> findAllByName(String name) {
        List<PostElastic> posts = postElasticRepository.findAllByName(name);
        return mapPostsToDTO(posts);
    }

    @Override
    public PostElastic findByName(String name) {
        return postElasticRepository.findByName(name);
    }

    @Override
    public List<PostResponseElastic> findAllByDescription(String description) {
        List<PostElastic> posts = postElasticRepository.findAllByDescription(description);
        return mapPostsToDTO(posts);
    }

    @Override
    public List<PostResponseElastic> findAllByDescriptionFromFile(String descriptionFromFile) {
        List<PostElastic> posts = postElasticRepository.findAllByDescriptionFromFile(descriptionFromFile);
        return mapPostsToDTO(posts);
    }

    @Override
    public List<PostResponseElastic> findByCommunityName(String name) {
        List<PostElastic> posts = postElasticRepository.findAllByCommunityName(name);
        return mapPostsToDTO(posts);
    }

    @Override
    public List<PostResponseElastic> findByKarma(Integer from, Integer to) {
        if(from == null || from < 0){
            from = 0;
        }
        if(to == null || to < 0){
            to = Integer.MAX_VALUE;
        }
        String range = from + "-" + to;
        QueryBuilder karmaQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("reactionCount", range));
        List<PostResponseElastic> response = PostESMapper.mapDtos(searchByBoolQuery(karmaQuery));

        return response;

    }

    @Override
    public List<PostResponseElastic> find(PostSearchParams params) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String operatorUpper = params.getOperator().toUpperCase();
        List<PostResponseElastic> response = new ArrayList<>();

        if ((!params.getName().isBlank()) && (!params.getDescription().isBlank())) {

            QueryBuilder findByName = SearchQueryGenerator.createMatchQueryBuilder(
                    new SimpleQueryEs("name", params.getName()));

            QueryBuilder findByDescription = SearchQueryGenerator.createMatchQueryBuilder(
                    new SimpleQueryEs("description", params.getDescription()));

            if (operatorUpper.equalsIgnoreCase("AND")) {
                boolQuery.must(findByName).must(findByDescription);
            } else {
                boolQuery.should(findByName).should(findByDescription);
            }
            response = PostESMapper.mapDtos(searchByBoolQuery(boolQuery));
        }


        if ((params.getMinKarma() != null || params.getMaxKarma() != null) && !params.getName().isBlank()) {

            Integer minNum = 0;
            if(params.getMinKarma() == null || params.getMinKarma() < 0){
                params.setMinKarma(minNum);

            }

            //Integer maxNum = params.getMaxNumOfPosts() != null ? params.getMaxNumOfPosts() : Integer.MAX_VALUE;
            Integer maxNum = Integer.MAX_VALUE ;
            if(params.getMaxKarma() == null || params.getMinKarma() < 0){
                params.setMaxKarma(maxNum);

            }

            String range = params.getMinKarma() + "-" + params.getMaxKarma();
            QueryBuilder karmaQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("reactionCount", range));

            QueryBuilder findByName = SearchQueryGenerator.createMatchQueryBuilder(
                    new SimpleQueryEs("name", params.getName()));

            if (operatorUpper.equalsIgnoreCase("AND")) {
                boolQuery.must(karmaQuery).must(findByName);
            } else if (operatorUpper.equalsIgnoreCase("OR")) {
                boolQuery.should(karmaQuery).should(findByName);
            }
            response = PostESMapper.mapDtos(searchByBoolQuery(boolQuery));
        }


        return response;
    }

    private SearchHits<PostElastic> searchByBoolQuery(QueryBuilder boolQueryBuilder) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, PostElastic.class,  IndexCoordinates.of("posts"));
    }

}
