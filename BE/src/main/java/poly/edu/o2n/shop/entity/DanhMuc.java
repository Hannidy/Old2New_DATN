package poly.edu.o2n.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "danh_muc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danh_muc_id")
    private Integer danhMucId;

    @Column(name = "ten_danh_muc", nullable = false, length = 150)
    private String tenDanhMuc;

    // Liên kết tự tham chiếu: Một danh mục có thể thuộc về một danh mục cha
    @ManyToOne
    @JoinColumn(name = "cha_id")
    private DanhMuc danhMucCha;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
