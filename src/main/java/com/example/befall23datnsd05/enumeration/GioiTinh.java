package com.example.befall23datnsd05.enumeration;

public enum GioiTinh {

    NAM(0),
    NU(1);

    private final Integer gioiTinh;

    GioiTinh(Integer gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Integer getGioiTinh() {
        return this.gioiTinh;
    }

    public String getDisplayName() {
        switch (this) {
            case NAM:
                return "Nam";
            case NU:
                return "Nữ";
            default:
                return this.name(); // Returns the default enum name if no corresponding name is found
        }
    }
}
