package Admin.Testcases.HocVien;

import org.openqa.selenium.WebDriver;
import org.json.simple.JSONObject;
import Admin.Pages.*;

public class ThemHocVien {
    WebDriver driver;
    AdminDashboardPage dashboard;
    HocVienPage hocVienPage;

    public ThemHocVien(WebDriver driver) {
        this.driver = driver;
        this.dashboard = new AdminDashboardPage(driver);
        this.hocVienPage = new HocVienPage(driver);
    }

    public String Tc_Them_Hoc_Vien(JSONObject hocVien) throws InterruptedException {
        dashboard.openQuanLyHocVien();
        Thread.sleep(2000); 

        // Lấy dữ liệu từ file JSON
        String hoTen = (String) hocVien.get("HoTen");
        String maHV = (String) hocVien.get("MaHocVien");
        String email = (String) hocVien.get("Email");
        String soDienThoai = (String) hocVien.get("SoDienThoai");
        String gioiTinh = (String) hocVien.get("GioiTinh");
        String ngaySinh = (String) hocVien.get("NgaySinh");
        String diaChi = (String) hocVien.get("DiaChi");

        hocVienPage.clickThemMoi();
        Thread.sleep(1000);
        
        hocVienPage.enterHoTen(hoTen);
        hocVienPage.enterMaHocVien(maHV);
        hocVienPage.enterEmail(email);
        hocVienPage.enterSoDienThoai(soDienThoai);    
        if ("Nam".equalsIgnoreCase(gioiTinh)) {
            hocVienPage.selectGioiTinhNam();
        } else if ("Nu".equalsIgnoreCase(gioiTinh) || "Nữ".equalsIgnoreCase(gioiTinh)) {
            hocVienPage.selectGioiTinhNu(); 
        } else if ("Khac".equalsIgnoreCase(gioiTinh) || "Khác".equalsIgnoreCase(gioiTinh)) {
            hocVienPage.selectGioiTinhKhac();
        }
        hocVienPage.enterNgaySinh(ngaySinh);
        hocVienPage.enterDiaChi(diaChi);

        hocVienPage.clickLuuThem();
        Thread.sleep(2000);
        String thongBao = hocVienPage.getThongBaoThanhCong();
        hocVienPage.clickOK();
        return thongBao;
    }
}