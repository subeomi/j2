package org.zerock.j2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@ToString(exclude = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String content;

    private String reviewer;

    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    
}
