package org.zerock.j2.dto;

import lombok.Data;

@Data
public class ProductListDTO {

    private Long pno;

    private String pname;

    private int price;
    
    // 상품이미지 이름
    private String fname;

    private long reviewCnt;

    private double reviewAvg;
    
}
