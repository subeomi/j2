package org.zerock.j2.repository.search;


import java.util.List;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.dto.PageResponseDTO;
import org.zerock.j2.dto.ProductListDTO;
import org.zerock.j2.entity.Product;
import org.zerock.j2.entity.QProduct;
import org.zerock.j2.entity.QProductImage;
import org.zerock.j2.entity.QProductReview;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch{

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, productImage);

        query.where(productImage.ord.eq(0));

        int pageNum = pageRequestDTO.getPage() <= 0? 0: pageRequestDTO.getPage() -1;
        Pageable pageable = 
         PageRequest.of( pageNum, pageRequestDTO.getSize(),
         Sort.by("pno").descending()  );

        this.getQuerydsl().applyPagination(pageable, query);

        log.info(query.fetch());

            JPQLQuery<ProductListDTO> dtoQuery = 
                query.select(
                    Projections.bean(ProductListDTO.class,
                    product.pno,
                    product.pname,
                    product.price,
                    productImage.fname)
                );
            List<ProductListDTO> dtoList = dtoQuery.fetch();

            long totalCount = dtoQuery.fetchCount();

        return new PageResponseDTO<>(dtoList, totalCount, pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ProductListDTO> listWithReview(PageRequestDTO pageRequestDTO) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QProductReview review = QProductReview.productReview;

        String keyword = pageRequestDTO.getKeyword();
        String searchType = pageRequestDTO.getType();

        JPQLQuery<Product> query = from(product);

        if(keyword != null && searchType != null){

            // tc -> [t, c]
            String[] searchArr = searchType.split("");

            // 우선순위 연산자 ( ... ) ...
            BooleanBuilder searchBuilder = new BooleanBuilder();

            for (String type : searchArr) {
                switch(type) {
                    // or연산
                    case "t" -> searchBuilder.or(product.pname.contains(keyword));
                    case "c" -> searchBuilder.or(product.pdesc.contains(keyword));
                    case "w" -> searchBuilder.or(product.writer.contains(keyword));
                }
            } // end for

            query.where(searchBuilder);
        }

        query.leftJoin(product.images, productImage);
        query.leftJoin(review).on(review.product.eq(product)); // on조건은 해당엔티티 기준에서

        query.where(productImage.ord.eq(0));
        query.where(product.delFlag.eq(Boolean.FALSE));

        int pageNum = pageRequestDTO.getPage() <= 0? 0: pageRequestDTO.getPage() -1;
        Pageable pageable = 
         PageRequest.of( pageNum, pageRequestDTO.getSize(),
         Sort.by("pno").descending()  );

        this.getQuerydsl().applyPagination(pageable, query);

        query.groupBy(product);

            JPQLQuery<ProductListDTO> dtoQuery = 
                query.select(
                    Projections.bean(ProductListDTO.class,
                    product.pno,
                    product.pname,
                    product.price,
                    productImage.fname.min().as("fname"),
                    review.score.avg().as("reviewAvg"),
                    review.count().as("reviewCnt"))
                );
            List<ProductListDTO> dtoList = dtoQuery.fetch();

            long totalCount = dtoQuery.fetchCount();

        return new PageResponseDTO<>(dtoList, totalCount, pageRequestDTO);

    }
    
}
