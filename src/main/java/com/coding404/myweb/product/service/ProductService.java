package com.coding404.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

public interface ProductService {

	public int regist(ProductVO vo, List<MultipartFile> list); //등록
	public ArrayList<ProductVO> getList(String user_id, Criteria cri); //조회: 특정회원정보만 조회
	public int getTotal(String user_id, Criteria cri);
	
	//카테고리 대분류처리
	public List<CategoryVO> getCategory();
	//카테고리 중분류, 소분류
	public List<CategoryVO> getCategoryChild(CategoryVO vo);
	
	//이미지데이터조회
	public List<ProductUploadVO> getProductImg(ProductVO vo);
}
