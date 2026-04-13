package Admin.Testcases.HocVien;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.simple.JSONObject;
import Admin.Pages.AdminDashboardPage;
import Admin.Pages.HocVienPage;
import java.time.Duration;

public class SoSanhHocVien {
    WebDriver driver;
    AdminDashboardPage dashboard;
    HocVienPage hocVienPage;

    public SoSanhHocVien(WebDriver driver) {
        this.driver = driver;
        this.dashboard = new AdminDashboardPage(driver);
        this.hocVienPage = new HocVienPage(driver);
    }

    public void Tc_So_Sanh_Hoc_Vien(JSONObject hocVien) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        dashboard.openQuanLyHocVien();
        wait.until(d -> hocVienPage.isHocVienPageDisplayed());

        String maHV = (String) hocVien.get("MaHocVien");
        hocVienPage.searchHocVien(maHV);
        wait.until(d -> hocVienPage.isSearchResultDisplayed());

        String actHoTen = hocVienPage.getHoTen();
        String actEmail = hocVienPage.getEmail();
        String actNgaySinh = hocVienPage.getNgaySinh();
        String actDiaChi = hocVienPage.getDiaChi();
        
        String expHoTen = (String) hocVien.get("HoTen");
        String expEmail = (String) hocVien.get("Email");
        String expNgaySinh = (String) hocVien.get("NgaySinh");
        String expDiaChi = (String) hocVien.get("DiaChi");

        boolean isKhopToanBo = true;

        if (actHoTen.equals(expHoTen)) {
            System.out.println("- Họ tên: Đúng");
        } else {
            System.out.println("- Họ tên: Không trùng khớp (Thực tế: " + actHoTen + " | Mong đợi: " + expHoTen + ")");
            isKhopToanBo = false;
        }

        if (actEmail.equals(expEmail)) {
            System.out.println("- Email: Đúng");
        } else {
            System.out.println("- Email: Không trùng khớp (Thực tế: " + actEmail + " | Mong đợi: " + expEmail + ")");
            isKhopToanBo = false;
        }

        if (actNgaySinh.equals(expNgaySinh)) {
            System.out.println("- Ngày sinh: Đúng");
        } else {
            System.out.println("- Ngày sinh: Không trùng khớp (Thực tế: " + actNgaySinh + " | Mong đợi: " + expNgaySinh + ")");
            isKhopToanBo = false;
        }

        if (actDiaChi.equals(expDiaChi)) {
            System.out.println("- Địa chỉ: Đúng");
        } else {
            System.out.println("- Địa chỉ: Không trùng khớp (Thực tế: " + actDiaChi + " | Mong đợi: " + expDiaChi + ")");
            isKhopToanBo = false;
        }

        if (isKhopToanBo) {
            System.out.println("=> KẾT LUẬN: Đúng (Khớp toàn bộ dữ liệu trong JSON)");
        } else {
            System.out.println("=> KẾT LUẬN: Không trùng khớp");
        }
    }
}