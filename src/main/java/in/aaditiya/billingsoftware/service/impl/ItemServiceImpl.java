package in.aaditiya.billingsoftware.service.impl;

import in.aaditiya.billingsoftware.entity.CategoryEntity;
import in.aaditiya.billingsoftware.entity.ItemEntity;
import in.aaditiya.billingsoftware.io.ItemRequest;
import in.aaditiya.billingsoftware.io.ItemResponse;
import in.aaditiya.billingsoftware.repository.CategoryRepository;
import in.aaditiya.billingsoftware.repository.ItemRepository;
import in.aaditiya.billingsoftware.service.CategoryService;
import in.aaditiya.billingsoftware.service.FileUploadService;
import in.aaditiya.billingsoftware.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final FileUploadService fileUploadService;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    @Override
    public ItemResponse add(ItemRequest request , MultipartFile file){
        String imageUrl = fileUploadService.uploadFile(file);
        ItemEntity newItem = convertToEntity(request);
        CategoryEntity existingCategory = categoryRepository.findByCategoryID((request.getCategoryId()))
                .orElseThrow(()->new RuntimeException("Category not found"));
        newItem.setCategory(existingCategory);
        newItem.setImageUrl(imageUrl);
        newItem = itemRepository.save(newItem);
        return convertToResponse(newItem);
    }
    private ItemEntity convertToEntity(ItemRequest request){
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }
    private ItemResponse convertToResponse(ItemEntity newItem){
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .name(newItem.getName())
                .description(newItem.getDescription())
                .price(newItem.getPrice())
                .imageUrl(newItem.getImageUrl())
                .categoryName(newItem.getCategory().getName())
                .categoryId(newItem.getCategory().getCategoryID())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();

    }
    @Override
   public List<ItemResponse> fetchItems(){
     return itemRepository.findAll()
        .stream()
        .map(itemEntity -> convertToResponse(itemEntity))
        .collect(Collectors.toList());
    }
    @Override
    public void deleteItem(String itemId){
          ItemEntity existingItem= itemRepository.findByItemId(itemId)
                  .orElseThrow(()-> new RuntimeException("Item not found"));
         boolean isDeleted=  fileUploadService.deleteFile(existingItem.getImageUrl());
         if(isDeleted){
             itemRepository.delete(existingItem);
         }
         else{
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete the image");
         }
    }

}
