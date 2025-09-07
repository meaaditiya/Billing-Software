package in.aaditiya.billingsoftware.io;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Data
@Builder
public class CategoryResponse {
    private String categoryID;
    private String name;
    private String description;
    private String bgColor;
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer items;
}
