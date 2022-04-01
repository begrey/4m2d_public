package com.samyeonyiduk.domain.titles;

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
@Table(name="title_list")
@Builder
public class TitleLists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //필드는 소문자로

    private String name;

    private String image;
}
