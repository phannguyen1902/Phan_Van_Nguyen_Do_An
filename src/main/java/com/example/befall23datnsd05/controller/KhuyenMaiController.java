package com.example.befall23datnsd05.controller;

import com.example.befall23datnsd05.dto.KhuyenMaiRequest;
import com.example.befall23datnsd05.entity.KhuyenMai;
import com.example.befall23datnsd05.enumeration.TrangThai;
import com.example.befall23datnsd05.enumeration.TrangThaiKhuyenMai;
import com.example.befall23datnsd05.service.ChiTietSanPhamService;
import com.example.befall23datnsd05.service.KhuyenMaiService;
import com.example.befall23datnsd05.worker.PrincipalKhachHang;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
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
@RequestMapping("/admin/khuyen-mai")
public class KhuyenMaiController {

    @Autowired
    KhuyenMaiService service;

    @Autowired
    ChiTietSanPhamService ctspService;

    List<TrangThaiKhuyenMai> list = new ArrayList<>(Arrays.asList(TrangThaiKhuyenMai.DANG_HOAT_DONG, TrangThaiKhuyenMai.DUNG_HOAT_DONG, TrangThaiKhuyenMai.SAP_DIEN_RA));

    private PrincipalKhachHang principalKhachHang = new PrincipalKhachHang();

    @GetMapping("")
    public String hienThi(Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("listKhuyenMai", service.getList());
        model.addAttribute("listTrangThai", list);
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/khuyen_mai";
    }

    @GetMapping("/trang-thai/{trangThai}")
    public String getByTrangThai(Model model,
                                 @PathVariable("trangThai") TrangThaiKhuyenMai trangThaiKhuyenMai) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("listTrangThai", list);
        model.addAttribute("listKhuyenMai", service.getByTrangThai(trangThaiKhuyenMai));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/khuyen_mai";
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
            return "redirect/admin/khuyen-mai";
        }
        model.addAttribute("listTrangThai", list);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("listKhuyenMai", service.findKhuyenMai(startDate, endDate, trangThaiKhuyenMai));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/khuyen_mai";
    }

    @GetMapping("/view-add")
    public String viewAdd(
            @ModelAttribute("khuyenMai") KhuyenMaiRequest khuyenMaiRequest,
            Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("khuyenMai", new KhuyenMai());
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/them_khuyen_mai";
    }


    @PostMapping("/add")
    public String them(@Valid @ModelAttribute("khuyenMai") KhuyenMaiRequest khuyenMaiRequest,
            BindingResult bindingResult, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        String ten = khuyenMaiRequest.getTen();
        if (bindingResult.hasErrors()) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khuyen_mai/them_khuyen_mai";
        } else {
            if (service.existsByTen(ten)) {
                model.addAttribute("errorTen", "Tên  đã tồn tại");
                model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
                return "admin-template/khuyen_mai/them_khuyen_mai";
            }
            service.add(khuyenMaiRequest);
            return "redirect:/admin/khuyen-mai";
        }
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(@PathVariable("id") Long id, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("khuyenMai", service.getById(id));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/sua_khuyen_mai";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("khuyenMai") KhuyenMaiRequest khuyenMaiRequest,
                         BindingResult bindingResult,
                         Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        String ten = khuyenMaiRequest.getTen();
        Long id = khuyenMaiRequest.getId();
        if (bindingResult.hasFieldErrors("ten") || bindingResult.hasFieldErrors("mucGiamGia")
                || bindingResult.hasFieldErrors("ngayKetThuc")) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khuyen_mai/sua_khuyen_mai";
        } else {
            if (service.existsByTenAndIdNot(ten, id)) {
                model.addAttribute("errorTen", "Tên  đã tồn tại");
                model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
                return "admin-template/khuyen_mai/sua_khuyen_mai";
            }
            service.update(khuyenMaiRequest);
            ctspService.autoUpdateGia();
            return "redirect:/admin/khuyen-mai";
        }
    }

    @GetMapping("/huy/{id}")
    public String huyKhuyenMai(@PathVariable("id") Long id) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        service.huy(id);
        ctspService.autoUpdateGia();
        return "redirect:/admin/khuyen-mai";
    }

    @GetMapping("/them-san-pham-khuyen-mai/{idKM}")
    public String sanPhamKhuyenMai(Model model,
                                   @PathVariable("idKM") Long idKM) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("ctspKhuyenMai", ctspService.getAllSanPhamKhuyenMai(idKM));
        model.addAttribute("ctspCoKhuyenMai", ctspService.getSpCoKhuyenMai(idKM));
        model.addAttribute("khuyenMai", service.getById(idKM));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khuyen_mai/san_pham_khuyen_mai";
    }

    @GetMapping("/them-san-pham-khuyen-mai/them/{idKM}/{idCtsp}")
    public String updateIdKhuyenMai(@PathVariable("idKM") Long idKM,
                                    @PathVariable("idCtsp") Long idCtsp) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        ctspService.updateIdKhuyenMai(idKM, idCtsp);
        ctspService.updateGiaBan(idCtsp);
        return "redirect:/admin/khuyen-mai/them-san-pham-khuyen-mai/{idKM}";
    }

    @GetMapping("/them-san-pham-khuyen-mai/xoa/{idKM}/{idCtsp}")
    public String deleteIdKhuyenMai(@PathVariable("idCtsp") Long idCtsp) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        ctspService.deleteIdKhuyenMai(idCtsp);
        ctspService.updateGiaBan(idCtsp);
        return "redirect:/admin/khuyen-mai/them-san-pham-khuyen-mai/{idKM}";
    }
}
