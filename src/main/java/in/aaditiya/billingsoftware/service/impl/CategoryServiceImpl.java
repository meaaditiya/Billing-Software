package in.aaditiya.billingsoftware.service.impl;

import in.aaditiya.billingsoftware.entity.CategoryEntity;
import in.aaditiya.billingsoftware.io.CategoryRequest;
import in.aaditiya.billingsoftware.io.CategoryResponse;
import in.aaditiya.billingsoftware.repository.CategoryRepository;
import in.aaditiya.billingsoftware.repository.ItemRepository;
import in.aaditiya.billingsoftware.service.CategoryService;
import in.aaditiya.billingsoftware.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final FileUploadService fileservice;
    private final ItemRepository itemRepository;
@Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file){
    String imageUrl = fileservice.uploadFile(file);
    CategoryEntity newCategory = convertToEntity(request);
    newCategory.setImageUrl(imageUrl);
    newCategory = repo.save(newCategory);
    return convertToResponse(newCategory);

}
@Override
    public List<CategoryResponse> read(){
    return repo.findAll().stream()
            .map(categoryEntity ->  convertToResponse(categoryEntity))
            .collect(Collectors.toList());
}
@Override
    public void delete(String categoryID){
   CategoryEntity currentEntity = repo.findByCategoryID(categoryID).orElseThrow(()-> new RuntimeException("category not found"));
   fileservice.deleteFile(currentEntity.getImageUrl());
    repo.delete(currentEntity);
}
private CategoryEntity convertToEntity(CategoryRequest request){
    return CategoryEntity.builder()
            .categoryID(UUID.randomUUID().toString())
            .name(request.getName())
            .description(request.getDescription())
            .bgColor(request.getBgColor())
            .build();
}
private CategoryResponse convertToResponse(CategoryEntity newCategory){
    Integer itemCount =  itemRepository.countByCategoryId(newCategory.getId());
    return CategoryResponse.builder()
            .categoryID(newCategory.getCategoryID())
            .name(newCategory.getName())
            .description(newCategory.getDescription())
            .bgColor(newCategory.getBgColor())
            .imageUrl(newCategory.getImageUrl())
            .createdAt(newCategory.getCreatedAt())
            .updatedAt(newCategory.getUpdatedAt())
            .items(itemCount)
            .build();

}
}
