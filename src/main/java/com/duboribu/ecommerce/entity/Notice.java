package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.front.enums.State;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Notice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String comment;
    @Enumerated(EnumType.STRING)
    private State state;
    private String filePath;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.PERSIST)
    List<Comment> commentList = new ArrayList<>();
}
