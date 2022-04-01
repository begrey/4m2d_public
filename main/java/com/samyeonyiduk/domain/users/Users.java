package com.samyeonyiduk.domain.users;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.posts.Posts;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name="user")
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //필드는 소문자로

    @Column(name="api_id")
    private Long apiId;

    @Column(name="intra_id")
    private String intraId;

    private String email;

    private String image;

    private int level;

    private double experience;

    private String introduce;

    @Column(name="user_title")
    @ColumnDefault("newb")
    private String userTitle;

    @Column(name="auth")
    @ColumnDefault("ROLE_USER")
    private String auth;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Posts> postsList;

    //METHOD

    public void patch(Map<String, Object> patchMap) {
        for(Map.Entry<String,Object> entry : patchMap.entrySet()){
            findKeyAndPatch(entry.getKey(), entry.getValue());
        }
    }

    public void findKeyAndPatch(String key, Object value) {
        switch (key) {
            case "introduce":
                this.introduce = value.toString();
                break;
            case "image":
                this.image = value.toString();
                break;
            case "level":
                this.level += (int)value;
                break;
            case "experience":
                this.experience = (double)value;
                break;
            default:
                throw new IllegalArgumentException("해당 column이 없습니다.");
        }
    }
}

