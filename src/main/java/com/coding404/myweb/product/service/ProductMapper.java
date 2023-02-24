package com.coding404.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

@Mapper //mapper인터페이스에는 @Mapper가 반드시 선언되야한다
public interface ProductMapper {
	//글등록
	public int regist(ProductVO vo);
	//파일업로드
	public int registFile(ProductUploadVO vo);
	//매개변수로 전달되는 데이터가 2개 이상이라면 @Param으로 이름붙이기 
	public ArrayList<ProductVO> getList(@Param("user_id") String user_id, @Param("cri") Criteria cri); //조회: 특정회원정보만 조회
	//전체게시글 수 
	public int getTotal(@Param("user_id")String user_id,  @Param("cri")Criteria cri); //전체게시글 수 

	//카테고리 대분류처리
	public List<CategoryVO> getCategory();
	//카테고리 중분류, 소분류
	public List<CategoryVO> getCategoryChild(CategoryVO vo);
	
	//이미지데이터조회
	public List<ProductUploadVO> getProductImg(ProductVO vo);
}
