package com.samyeonyiduk.domain.posts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.samyeonyiduk.domain.category.Category;
import com.samyeonyiduk.domain.comments.Comments;
import com.samyeonyiduk.domain.users.Users;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="post")
@Builder
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //필드는 소문자로

    private String title;

    private String content;

    @Column(name = "image_num")
    @ColumnDefault("0")
    private int imageNum;

    private String image;

    @Column(nullable = false)
    private int price;

    private int subscribes;

    private int view;

    private String local;

    private int status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name ="user_id")
    private Users user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private final List<Comments> commentList = new ArrayList<>();

    public void patch(Map<String, Object> patchMap) {
        for(Map.Entry<String,Object> entry : patchMap.entrySet()){
            findKeyAndPatch(entry.getKey(), entry.getValue());
        }
    }

    public void findKeyAndPatch(String key, Object value) {
        switch(key) {
            case "title" :
                this.title = value.toString();
                break;
            case "content" :
                this.content = value.toString();
                break;
            case "imageNum" :
                this.imageNum = (int)value;
                break;
            case "image" :
                this.image = value.toString();
                break;
            case "price" :
                this.price = (int)value;
                break;
            case "local" :
                this.local = value.toString();
                break;
            case "status" :
                this.status =(int)value;
                break;
            case "subscribes" :
                this.subscribes += (int)value;
                break;
            case "view" :
                this.view += (int)value;
                break;
            case "category" :
                this.category = (Category) value;
                break;
            case "updatedAt" :
                this.updatedAt = LocalDateTime.now();
                break;
            default:
                throw new IllegalArgumentException("해당 column: " + key + " 이 없습니다.");
        }
    }
}
