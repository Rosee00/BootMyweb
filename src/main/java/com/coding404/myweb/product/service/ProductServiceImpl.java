package com.coding404.myweb.product.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

@Service("productService")
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductMapper productMapper;
	
	//업로드패스
	@Value("${project.uploadpath}")
	private String uploadpath;
	
	//날짜별로 폴더생성
	public String makeDir() {
		//날짜형식변경
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String now = sdf.format(date); //오늘날짜
		
		//업로드폴더 하위
		String path = uploadpath + "\\" + now; //경로 설정
		File file = new File(path);
		
		if(file.exists() == false) { //존재하면 true
			file.mkdir(); //폴더생성
		}	
		return now;
	}
	
	
	//상품등록
	//한프로세스 안에서 예외가 발생하면, 기존에 진행했던 CRUD작업을 Rollback시킵니다
	//조건 - catch를 통해서 예외처리가 진행되면 트랜잭션 처리가 되지 않습니다.
	@Transactional(rollbackFor = Exception.class) //@Transactional(rollbackFor = 롤백조건)
	@Override
	public int regist(ProductVO vo, List<MultipartFile> list ) {
		
		//1.글 등록처리
		int result = productMapper.regist(vo);
		
		//2. 파일인서트 ->
		//반복처리 : 빈 첨부파
		for(MultipartFile file : list) {
			//파일명
			String origin = file.getOriginalFilename(); 
			//브라우저별로 경로가 포함되서 올라는 경우가 있어서 간단한 처리
			String filename = origin.substring(origin.lastIndexOf("\\") + 1); 		
			//폴더생성
			String filepath = makeDir();
			//중복파일의 처리
			String uuid = UUID.randomUUID().toString(); //랜덤한 uuid 
			//최종저장경로
			String savename = uploadpath + "\\" + filepath + "\\" + uuid + "_" + filename; //최초로 발견되는 _뒤가 폴더명
									
			try {
				File save = new File(savename); //세이브경로 in (최종적으로 저장된 파일경로)
				file.transferTo(save); //업로드진행
				
			} catch (Exception e) {
				e.printStackTrace();
				return 0; //실패의 의미로 0
			}
			
			
			//인서트(파일경로) - insert이전에 prod_id가 필요한데, selectKey방식으로 처리
			ProductUploadVO prodVO = ProductUploadVO.builder()
												   .filename(filename)
												   .filepath(filepath)
												   .uuid(uuid)	
												   .prod_writer(vo.getProd_writer())
												   .build();
					
			productMapper.registFile(prodVO);
			
		} //end for문
		
		return result; //성공시 1, 실패시 0
	}
	//
	@Override
	public ArrayList<ProductVO> getList(String user_id, Criteria cri) {
		return productMapper.getList(user_id, cri);
	}
	//전체게시글 수 
	@Override
	public int getTotal(String user_id, Criteria cri) {
		return productMapper.getTotal(user_id, cri);
	}
	
	
	//카테고러 대분류처리
	@Override
	public List<CategoryVO> getCategory() {
		return productMapper.getCategory();
	}
	//카테고리 중분류, 소분류
	@Override
	public List<CategoryVO> getCategoryChild(CategoryVO vo) {
		return productMapper.getCategoryChild(vo);
	}
	
	//이미지데이터조회
	@Override
	public List<ProductUploadVO> getProductImg(ProductVO vo) {
		return productMapper.getProductImg(vo);
	}
	
}
