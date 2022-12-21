package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
        throws Exception{

        //상품 등록
        //상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성
        Item item = itemFormDto.createItem();
        //상품 데이터를 저장
        itemRepository.save(item);

        //이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            //첫번째 이미지일 경우 대표 상품 이미지 여부값을 "Y"로 세팅. 나머지 상품 이미지 = N
            if (i ==0){
                itemImg.setRepImgYn("Y");
            }else {
                itemImg.setRepImgYn("N");
            }
            //상품의 이미지 정보를 저장
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }
        return item.getId();
    }

    // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용을 설정., 이럴경우 JPA가 더티체킹(변경감지)을
    //수행하지 않아서 성능을 향상 시킨다.
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        // 해당 상품의 이미지를 조회한다. 등록순으로 가지고 오기 위해ㅓㅅ 사품 이미지 아이디 오름차순으로 가지고 온다.
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가한다.
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품의 아이디를 통해 상품 엔티티를 조회. 존재하지 않을 때는 EntityNotFoundException을 발생
        Item item = itemRepository.findById(itemId).
                orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
        throws Exception{

        //상품 수정
        //상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        //상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트
        item.updateItem(itemFormDto);
        //상품 이미지 아이디 리스트를 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        //이미지등록
        for (int i=0; i<itemImgFileList.size();i++){
            //상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와,
            //상품 이미지 파일 정보를 파라미터로 전달
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

}
