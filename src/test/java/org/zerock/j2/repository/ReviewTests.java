package org.zerock.j2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.j2.entity.Product;
import org.zerock.j2.entity.ProductReview;

@SpringBootTest
public class ReviewTests {
    
    @Autowired
    ProductReviewRepository repository;

    @Test
    public void insertReview(){
        Long[] pnoArr = {216L};

        for(Long pno : pnoArr){

            int score = 5;
//            int score = (int)(Math.random() * 5) + 1;

            Product product = Product.builder().pno(pno).build();

            for(int i = 0; i < 5; i++){

                ProductReview review = ProductReview.builder()
                    .content("skoaskdaoskdoas")
                    .reviewer("user"+i)
                    .score(score)
                    .product(product)
                    .build();
                
                repository.save(review);

            }// end for

        }// end for
    }

}
