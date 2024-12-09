package com.example.befall23datnsd05.controller;

import com.example.befall23datnsd05.enumeration.TrangThai;
import com.example.befall23datnsd05.request.ThuongHieuRequest;
import com.example.befall23datnsd05.service.ThuongHieuService;
import com.example.befall23datnsd05.worker.PrincipalKhachHang;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/thuong-hieu")
public class ThuongHieuController {

    private final ThuongHieuService thuongHieuService;

    public ThuongHieuController(ThuongHieuService thuongHieuService) {
        this.thuongHieuService = thuongHieuService;
    }

    Integer pageNo = 0;

    List<TrangThai> list = new ArrayList<>(Arrays.asList(TrangThai.DANG_HOAT_DONG, TrangThai.DUNG_HOAT_DONG));

    private PrincipalKhachHang principalKhachHang = new PrincipalKhachHang();

    @GetMapping()
    public String getAll(Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("listThuongHieu", thuongHieuService.getList());
        model.addAttribute("index", pageNo + 1);
        model.addAttribute("trangThais", list);
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/thuong_hieu/thuong_hieu";
    }

    @GetMapping("/trang-thai/{trangThai}")
    public String getByTrangThai(Model model,
                                 @PathVariable("trangThai") TrangThai trangThai) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("trangThais", list);
        model.addAttribute("listThuongHieu", thuongHieuService.getByTrangThai(trangThai));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/thuong_hieu/thuong_hieu";
    }

    @GetMapping("/view-add-thuong-hieu")
    public String getViewAdd(@ModelAttribute("thuongHieu") ThuongHieuRequest thuongHieu, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/thuong_hieu/them_thuong_hieu";
    }

    @PostMapping("/add")
    public String addNew(@Valid @ModelAttribute("thuongHieu") ThuongHieuRequest thuongHieu,
                         BindingResult bindingResult, Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        String ma = thuongHieu.getMa();
        String ten = thuongHieu.getTen();
        if (bindingResult.hasErrors()) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/thuong_hieu/them_thuong_hieu";
        }
        if (thuongHieuService.existByMa(ma) && thuongHieuService.existsByTen(ten)) {
            model.addAttribute("errorMa", "Mã  đã tồn tại");
            model.addAttribute("errorTen", "Tên  đã tồn tại");
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/thuong_hieu/them_thuong_hieu";
        }
        if (thuongHieuService.existByMa(ma)) {
            model.addAttribute("errorMa", "Mã  đã tồn tại");
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/thuong_hieu/them_thuong_hieu";
        }
        if (thuongHieuService.existsByTen(ten)) {
            model.addAttribute("errorTen", "Tên  đã tồn tại");
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/thuong_hieu/them_thuong_hieu";
        }
        model.addAttribute("success", "Thêm thành công");

        thuongHieuService.save(thuongHieu);
        return "redirect:/admin/thuong-hieu?success";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        try {
            thuongHieuService.remove(id);
            model.addAttribute("success", "Xóa thành công");
            return "redirect:/admin/thuong-hieu?success";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Không thể xóa bản ghi vì có ràng buộc khóa ngoại.");
            return "redirect:/admin/thuong-hieu?errorMessage";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi xóa bản ghi.");
            return "redirect:/admin/thuong-hieu?errorMessage";
        }

    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(@PathVariable("id") Long id,
                             Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("thuongHieu", thuongHieuService.findById(id));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/thuong_hieu/sua_thuong_hieu";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("thuongHieu") ThuongHieuRequest thuongHieu,
                         BindingResult bindingResult, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        String ten = thuongHieu.getTen();
        Long id = thuongHieu.getId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/thuong_hieu/sua_thuong_hieu";
        } else {
            if (thuongHieuService.existsByTenAndIdNot(ten, id)) {
                model.addAttribute("errorTen", "Tên  đã tồn tại");
                model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
                return "admin-template/thuong_hieu/sua_thuong_hieu";
            }
            model.addAttribute("success", "Thêm thành công");

            thuongHieuService.update(thuongHieu);
            return "redirect:/admin/thuong-hieu?success";
        }
    }
}

