package com.ftn.RedditClone.model.entity.elastic;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "posts")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class PostElastic {

    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Keyword)
    private String titleFromFile;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private String descriptionFromFile;
    @Field(type = FieldType.Text)
    private String communityName;
    @Field(type = FieldType.Integer)
    private Integer reactionCount;

    @Field(type = FieldType.Keyword)
    private String keywords;

    private String filename;
}
