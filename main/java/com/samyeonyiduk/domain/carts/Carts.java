package com.samyeonyiduk.domain.carts;

import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart")
@Builder
public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name ="post_id")
    private Posts post;
}
