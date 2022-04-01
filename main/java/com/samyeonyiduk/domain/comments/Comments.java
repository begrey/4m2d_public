package com.samyeonyiduk.domain.comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor //entity class는 기본생성자로 객체를 생성하기 때문에 필요하다.
@Entity
@Table(name = "comment")
@DynamicInsert
@DynamicUpdate
@Builder
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

    private String content;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    public void update(Object object) {
        this.content = object.toString();
    }
}
