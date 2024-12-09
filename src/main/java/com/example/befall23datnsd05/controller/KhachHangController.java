package com.example.befall23datnsd05.controller;

import com.example.befall23datnsd05.dto.DiaChiRequest;
import com.example.befall23datnsd05.dto.KhachHangRequest;
import com.example.befall23datnsd05.entity.DiaChi;
import com.example.befall23datnsd05.entity.KhachHang;
import com.example.befall23datnsd05.enumeration.TrangThai;
import com.example.befall23datnsd05.service.DiaChiService;
import com.example.befall23datnsd05.service.KhachHangService;
import com.example.befall23datnsd05.worker.PrincipalKhachHang;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private DiaChiService diaChiService;

    Integer pageNo = 0;

    List<TrangThai> list = new ArrayList<>(Arrays.asList(TrangThai.DANG_HOAT_DONG, TrangThai.DUNG_HOAT_DONG));

    private PrincipalKhachHang principalKhachHang = new PrincipalKhachHang();

    @GetMapping
    public String getAll(Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("listKH", khachHangService.getList());
        model.addAttribute("trangThais", list);
        model.addAttribute("diaChi", new DiaChiRequest());
        model.addAttribute("listDC", diaChiService.getAll());
        model.addAttribute("index", pageNo + 1);
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khach_hang/khach_hang";
    }

    @GetMapping("/trang-thai/{trangThai}")
    public String getByTrangThai(Model model,
                                 @PathVariable("trangThai") TrangThai trangThai) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        model.addAttribute("trangThais", list);
        model.addAttribute("diaChi", new DiaChiRequest());
        model.addAttribute("listDC", diaChiService.getAll());
        model.addAttribute("listKH", khachHangService.getByTrangThai(trangThai));
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khach_hang/khach_hang";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(@PathVariable("id") Long id, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        KhachHang khachHang = khachHangService.getById(id);
        model.addAttribute("listDC", diaChiService.getAllTheoKhachHang(id));
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("idKhachHang", id);
        model.addAttribute("diaChi", new DiaChiRequest());
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khach_hang/sua_khach_hang";
    }

    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        model.addAttribute("khachHang", new KhachHangRequest());
        model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
        return "admin-template/khach_hang/them_khach_hang";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachHang") KhachHangRequest khachHangRequest,
                      BindingResult bindingResult,
                      Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        String sdt = khachHangRequest.getSdt();
        if (bindingResult.hasErrors()) {
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khach_hang/them_khach_hang";
        }
        if (khachHangService.existsBySdt(sdt)) {
            model.addAttribute("errorTen", "Số điện thoại đã tồn tại");
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khach_hang/them_khach_hang";
        }
        model.addAttribute("success", "Thêm thành công");
        khachHangService.add(khachHangRequest);
        return "redirect:/admin/khach-hang?success";

    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        try {
            khachHangService.remove(id);
            model.addAttribute("success", "Xóa thành công");
            return "redirect:/admin/khach-hang?success";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Không thể xóa bản ghi vì có ràng buộc khóa ngoại.");
            return "redirect:/admin/khach-hang?errorMessage";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi xóa bản ghi.");
            return "redirect:/admin/khach-hang?errorMessage";
        }

    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("khachHang") KhachHangRequest khachHangRequest,
                         BindingResult bindingResult,
                         Model model) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        Long id = khachHangRequest.getId();
        String sdt = khachHangRequest.getSdt();
        if (bindingResult.hasErrors()) {
            model.addAttribute("diaChi",new DiaChiRequest());
            model.addAttribute("listDC", diaChiService.getAllTheoKhachHang(id));
            model.addAttribute("idKhachHang", id);
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khach_hang/sua_khach_hang";
        }

        if (khachHangService.existsBySdtAndIdNot(sdt, id)) {
            model.addAttribute("errorTen", "Số điện thoại đã tồn tại");
            model.addAttribute("diaChi",new DiaChiRequest());
            model.addAttribute("khachHang", khachHangService.getById(id));
            model.addAttribute("idKhachHang", id);
            model.addAttribute("listDC", diaChiService.getAllTheoKhachHang(id));
            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
            return "admin-template/khach_hang/sua_khach_hang";
        }
        model.addAttribute("success", "Cập nhật thành công!");
        khachHangService.update(khachHangRequest);
        return "redirect:/admin/khach-hang?success";

    }
//    @PostMapping("/update")
//    public String update(@Valid @ModelAttribute("khachHang") KhachHangRequest khachHangRequest,
//                         BindingResult bindingResult,
//                         Model model) {
//        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
//        if (idNhanVien == null){
//            return "redirect:/login";
//        }
//        Long id = khachHangRequest.getId();
//        String sdt = khachHangRequest.getSdt();
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
//            return "admin-template/khach_hang/sua_khach_hang";
//        }
//
//        if (khachHangService.existsBySdtAndIdNot(sdt, id)) {
//            model.addAttribute("errorTen", "Số điện thoại đã tồn tại");
//            model.addAttribute("tenNhanVien", principalKhachHang.getCurrentNhanVienTen());
//            return "admin-template/khach_hang/sua_khach_hang";
//        }
//        model.addAttribute("success", "Cập nhật thành công!");
//        khachHangService.update(khachHangRequest);
//        return "redirect:/admin/khach-hang?success";
//
//    }

    @PostMapping("/add-dia-chi/{idKhachHang}")
    public String addDiaChi(
            @Valid
            @ModelAttribute("diaChi") DiaChiRequest diaChiRequest,
            @PathVariable("idKhachHang") String idKhachHang,
            @RequestParam("phuongXaID") String phuongXa,
            @RequestParam("quanHuyenID") String quanHuyen,
            @RequestParam("thanhPhoID") String thanhPho
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        diaChiService.add(diaChiRequest, Long.valueOf(idKhachHang), thanhPho, quanHuyen, phuongXa);
        return "redirect:/admin/khach-hang/view-update/" + idKhachHang + "?success";
    }

    @PostMapping("/update-dia-chi/{id}/{idKH}")
    public String updateDiaChi(
            @PathVariable("id") Long id,
            @PathVariable("idKH") Long idKH,
            @ModelAttribute("diaChi") DiaChiRequest diaChiRequest,
            @RequestParam("phuongXa") String phuongXa,
            @RequestParam("quanHuyen") String quanHuyen,
            @RequestParam("thanhPho") String thanhPho,
            Model model
    ) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        diaChiService.update(diaChiRequest, thanhPho, quanHuyen, phuongXa);
        return "redirect:/admin/khach-hang/view-update/" + idKH;
    }

    @GetMapping("/delete-dia-chi/{id}/{idKH}")
    public String deleteDiaChi(@PathVariable("id") Long id,
                               @PathVariable("idKH") Long idKH) {
        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        diaChiService.remove(id);
        return "redirect:/admin/khach-hang/view-update/" + idKH;
    }

}