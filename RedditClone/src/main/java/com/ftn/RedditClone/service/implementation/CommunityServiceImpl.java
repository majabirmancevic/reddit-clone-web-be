package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.elasticRepository.CommunityElasticRepository;
import com.ftn.RedditClone.elasticRepository.PostElasticRepository;
import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.lucene.indexing.handlers.*;
import com.ftn.RedditClone.mapper.CommunityMapper;
import com.ftn.RedditClone.mapper.elastic.CommunityESMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Moderator;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.model.entity.dto.CommunityResponseElastic;
import com.ftn.RedditClone.model.entity.dto.SimpleQueryEs;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import com.ftn.RedditClone.repository.CommunityRepository;
import com.ftn.RedditClone.service.CommunityService;
import com.ftn.RedditClone.service.ModeratorService;
import com.ftn.RedditClone.service.SearchQueryGenerator;
import com.ftn.RedditClone.service.UserService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class CommunityServiceImpl implements CommunityService {

    @Value("${files.path}")
    private String filesPath;
    CommunityRepository communityRepository;
    @Autowired
    CommunityElasticRepository communityElasticRepository;

    @Autowired
    PostElasticRepository postElasticRepository;


   // CommunityESMapper communityESMapper;

    CommunityMapper communityMapper;

    UserService userService;

    ModeratorService moderatorService;

    ElasticsearchRestTemplate elasticsearchRestTemplate;

    public CommunityServiceImpl (CommunityRepository communityRepository, CommunityMapper communityMapper, UserService userService,ModeratorService moderatorService,CommunityElasticRepository communityElasticRepository,ElasticsearchRestTemplate elasticsearchRestTemplate){
        this.communityMapper = communityMapper;
        this.communityRepository = communityRepository;
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.communityElasticRepository = communityElasticRepository;
      //  this.communityESMapper = esMapper;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public CommunityDto save(CommunityDto communityDto) throws IOException {
        Community community =  communityMapper.mapDtoToSubreddit(communityDto);
        communityDto.setId(community.getId());
        community.setCreationDate(String.valueOf(LocalDate.now()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        community.setUser(user);

        Moderator moderator = new Moderator();

        moderator.setUser(user);
        moderator.setCommunity(community);

        communityRepository.save(community);
        indexUploadedFile(communityDto);

        return communityDto;
    }

    public void indexUploadedFile(CommunityDto dto) throws IOException {
        List<PostElastic> posts = new ArrayList<PostElastic>();

        if (dto.getFiles() == null){
            CommunityElastic communityIndexUnit = new CommunityElastic();
            communityIndexUnit.setName(dto.getName());
            communityIndexUnit.setDescription(dto.getDescription());
            communityIndexUnit.setPosts(posts);
            communityIndexUnit.setNumOfPosts(communityIndexUnit.getPosts().size());
            index(communityIndexUnit);
        }else{
            for (MultipartFile file : dto.getFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String fileName = saveUploadedFileInFolder(file);
                if(fileName != null){
                    CommunityElastic communityIndexUnit = getHandler(fileName).getIndexUnitCommunity(new File(fileName));
                    communityIndexUnit.setName(dto.getName());
                    communityIndexUnit.setDescription(dto.getDescription());
                    communityIndexUnit.setPosts(posts);
                    communityIndexUnit.setNumOfPosts(communityIndexUnit.getPosts().size());
                    index(communityIndexUnit);
                }
            }
        }
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
            Path path = Paths.get(new File(filesPath).getAbsolutePath() + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }

    @Override
    public Community save(Community community) {
        Community comm =  communityRepository.save(community);
        return comm;
    }

    public List<CommunityDto> getAll() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Override
    public CommunityDto getCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        CommunityDto dto = communityMapper.mapSubredditToDto(community);
        dto.setUserId(community.getUser().getId());

        return dto;
    }

    @Override
    public CommunityDto getCommunityByName(String name) {
        Community community = communityRepository.findByName(name);
        return communityMapper.mapSubredditToDto(community);
    }

    @Override
    public Community removeCommunity(Long id, String suspendedReason) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        community.setSuspended(true);
        community.setSuspendedReason(suspendedReason);
        community.setUser(null);
        return communityRepository.save(community);
    }

    @Override
    public Community findCommunity(Long id) {
        return communityRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<CommunityDto> getAllNotDeleted() {
        return communityRepository.findAllBySuspended()
                .stream()
                .map(communityMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Override
    public Community editCommunity(CommunityDto dto, Long id) {

        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));

        if(community != null){
            if (dto.getDescription() != "") {
                community.setDescription(dto.getDescription());
            }
            if(dto.getName() != ""){
                community.setName(dto.getName());
            }
            communityRepository.save(community);
        }
        return null;
    }

    @Override
    public List<CommunityResponseElastic> findAllByName(String name) {
        List<CommunityElastic> communities = communityElasticRepository.findAllByName(name);
        return mapCommunitiesToDTO(communities);
    }

    @Override
    public List<CommunityResponseElastic> findAllByDescription(String description) {
        List<CommunityElastic> communities = communityElasticRepository.findAllByDescription(description);
        return mapCommunitiesToDTO(communities);
    }

    @Override
    public List<CommunityResponseElastic> findAllByDescriptionFromFile(String descriptionFromFile) {
        List<CommunityElastic> communities = communityElasticRepository.findAllByDescriptionFromFile(descriptionFromFile);
        return mapCommunitiesToDTO(communities);
    }



    public void index(CommunityDto bookDTO){
        communityElasticRepository.save(CommunityESMapper.mapDtoToCommunity(bookDTO));
    }
    public void index(CommunityElastic communityElastic) {
        communityElasticRepository.save(communityElastic);
    }

    // REINDEX

    public void reindex(){
        communityElasticRepository.deleteAll();
        //elasticsearchRestTemplate.indexOps(IndexCoordinates.of("communities")).delete();
        elasticsearchRestTemplate.indexOps(CommunityElastic.class).delete();
        elasticsearchRestTemplate.indexOps(CommunityElastic.class).create();
    }



    public void reindexFromFile() {
        File dataDir = new File(filesPath);
        indexUnitFromFile(dataDir);
    }

    public int indexUnitFromFile(File file) {
        DocumentHandler handler;
        String fileName;
        int retVal = 0;
        try {
            File[] files;
            if(file.isDirectory()){
                files = file.listFiles();
            }else{
                files = new File[1];
                files[0] = file;
            }
            assert files != null;
            for(File newFile : files){
                if(newFile.isFile()){
                    fileName = newFile.getName();
                    handler = getHandler(fileName);
                    if(handler == null){
                        System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
                        continue;
                    }
                    index(handler.getIndexUnitCommunity(newFile));
                    retVal++;
                } else if (newFile.isDirectory()){
                    retVal += indexUnitFromFile(newFile);
                }
            }
            System.out.println("indexing done");
        } catch (Exception e) {
            System.out.println("indexing NOT done");
        }
        return retVal;
    }

    private List<CommunityResponseElastic> mapCommunitiesToDTO(List<CommunityElastic> communities){
        List<CommunityResponseElastic> communitiesDTO = new ArrayList<>();

        for(CommunityElastic community : communities){

            CommunityResponseElastic response = CommunityResponseElastic.builder()
                    .id(community.getId())
                    .name(community.getName())
                    .numOfPosts(community.getPosts().size())
                    .averageKarma(CommunityESMapper.averageKarma(postElasticRepository.findAllByCommunityName(community.getName())))
                    .build();

            communitiesDTO.add(response);
//            communitiesDTO.add(CommunityESMapper.mapCommunityToDto(community));
        }
        return communitiesDTO;
    }

    @Override
    public List<CommunityResponseElastic> findByNumOfPosts(int from, int to) {
        String range = from + "-" + to;
        QueryBuilder numbOfPostsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("numOfPosts", range));
        List<CommunityResponseElastic> response = CommunityESMapper.mapDtos(searchByBoolQuery(numbOfPostsQuery));

        for(CommunityResponseElastic community : response){
            community.setAverageKarma(CommunityESMapper.averageKarma(postElasticRepository.findAllByCommunityName(community.getName())));
        }
        return response;

    }

    @Override
    public void deleteAll() {
        communityElasticRepository.deleteAll();
    }

    private SearchHits<CommunityElastic> searchByBoolQuery(QueryBuilder boolQueryBuilder) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, CommunityElastic.class,  IndexCoordinates.of("communities"));
    }
}
