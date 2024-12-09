package com.example.befall23datnsd05.controller;

import com.example.befall23datnsd05.dto.KhuyenMaiRequest;
import com.example.befall23datnsd05.dto.MaGiamGiaRequest;
import com.example.befall23datnsd05.entity.KhuyenMai;
import com.example.befall23datnsd05.entity.MaGiamGia;
import com.example.befall23datnsd05.enumeration.TrangThai;
import com.example.befall23datnsd05.enumeration.TrangThaiKhuyenMai;
import com.example.befall23datnsd05.service.MaGiamGiaService;
import com.example.befall23datnsd05.worker.PrincipalKhachHang;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/ma-giam-gia")
public class MaGiamGiaController {

    @Autowired
    private MaGiamGiaService service;

    List<TrangThaiKhuyenMai> list = new ArrayList<>(Arrays.asList(TrangThaiKhuyenMai.DANG_HOAT_DONG, TrangThaiKhuyenMai.DUNG_HOAT_DONG, TrangThaiKhuyenMai.SAP_DIEN_RA));

    private PrincipalKhachHang principalKhachHang = new PrincipalKhachHang();

    @GetMapping()
    public String getAll(Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("listMaGiam", service.getAll());
        model.addAttribute("listTrangThai", list);
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/ma_giam_gia/ma_giam_gia";
    }

    @GetMapping("/trang-thai/{trangThai}")
    public String getByTrangThai(Model model,
                                 @PathVariable("trangThai") TrangThaiKhuyenMai trangThaiKhuyenMai) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("listMaGiam", service.getByTrangThai(trangThaiKhuyenMai));
        model.addAttribute("listTrangThai", list);
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/ma_giam_gia/ma_giam_gia";
    }

    @GetMapping("/filter")
    public String filterNgay(Model model,
                             @Param("trangThai") TrangThaiKhuyenMai trangThaiKhuyenMai,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        if (startDate.isAfter(endDate)) {
            model.addAttribute("listTrangThai", list);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "redirect/admin/ma-giam-gia";
        }
        model.addAttribute("listTrangThai", list);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("listMaGiam", service.findMaGiamGia(startDate, endDate, trangThaiKhuyenMai));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/ma_giam_gia/ma_giam_gia";
    }

    @GetMapping("/view-add")
    public String viewAdd(
            @ModelAttribute("maGiamGia") MaGiamGiaRequest maGiamGiaRequest,
            Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("maGiamGia", new MaGiamGia());
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/ma_giam_gia/them_ma_giam_gia";
    }

    @PostMapping("/add")
    public String them(
            @Valid
            @ModelAttribute("maGiamGia") MaGiamGiaRequest maGiamGiaRequest,
            BindingResult bindingResult,
            Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        String ten = maGiamGiaRequest.getTen();
        if (bindingResult.hasErrors()) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/ma_giam_gia/them_ma_giam_gia";
        } else {
            if (service.existsByTen(ten)) {
                model.addAttribute("errorTen", "Tên  đã tồn tại");
                model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
                return "admin-template/ma_giam_gia/them_ma_giam_gia";
            }
            service.add(maGiamGiaRequest);
            return "redirect:/admin/ma-giam-gia";
        }
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(
            @PathVariable("id") Long id,
            Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("maGiamGia", service.getById(id));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/ma_giam_gia/sua_ma_giam_gia";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("maGiamGia") MaGiamGiaRequest maGiamGiaRequest,
                         BindingResult bindingResult,
                         Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        String ten = maGiamGiaRequest.getTen();
        Long id = maGiamGiaRequest.getId();
        if (bindingResult.hasFieldErrors("ten") || bindingResult.hasFieldErrors("mucGiamGia")
                || bindingResult.hasFieldErrors("mucGiamToiDa") || bindingResult.hasFieldErrors("soLuong")
                || bindingResult.hasFieldErrors("giaTriDonHang") || bindingResult.hasFieldErrors("ngayKetThuc")) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/ma_giam_gia/sua_ma_giam_gia";
        } else {
            if (service.existsByTenAndIdNot(ten, id)) {
                model.addAttribute("errorTen", "Tên  đã tồn tại");
                model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
                return "admin-template/ma_giam_gia/sua_ma_giam_gia";
            }
            service.update(maGiamGiaRequest);
            return "redirect:/admin/ma-giam-gia";
        }

    }

    @GetMapping("/huy/{id}")
    public String huy(@PathVariable("id") Long id) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        service.huy(id);
        return "redirect:/admin/ma-giam-gia";
    }

}
