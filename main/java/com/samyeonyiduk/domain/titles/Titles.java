package com.samyeonyiduk.domain.titles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.samyeonyiduk.domain.users.Users;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="title")
@Builder
public class Titles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //필드는 소문자로

    private String name;

    private String image;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private Users user;
}
