package com.example.befall23datnsd05.entity;

import com.example.befall23datnsd05.enumeration.GioiTinh;
import com.example.befall23datnsd05.enumeration.TrangThai;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.EnumType.ORDINAL;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ma", unique = true)
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "sdt", unique = true)
    private String sdt;

    @Column(name = "gioi_tinh")
    @Enumerated(ORDINAL)
    private GioiTinh gioiTinh;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_sua")
    private LocalDate ngaySua;

    @Column(name = "email")
    private String email;

    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "tich_diem", precision = 19, scale = 2)
    private BigDecimal tichDiem;

    @Column(name = "trang_thai")
    @Enumerated(ORDINAL)
    private TrangThai trangThai;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<DiaChi> listDiaChi;

    public String getMainDiaChi(){

        return this.listDiaChi.get(0).getDiaChi();
    }

}