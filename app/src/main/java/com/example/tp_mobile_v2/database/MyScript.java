package com.example.tp_mobile_v2.database;

/**
 * Created by LapTrinhMobile on 10/12/2015.
 */
public class MyScript {

    private String CreateHoaDon, CreateThanhToanHoaDon, CreateCTHoaDon;

// "CREATE TABLE [{0}](\n" +
    public MyScript() {
        CreateHoaDon = "(\n" +
                "      [HoaDonID]      NVARCHAR(36) NOT NULL PRIMARY KEY,\n" +
                "\t  [MaHD]          NVARCHAR(20) NOT NULL,\n" +
                "\t  [STT]           BIGINT NOT NULL,\n" +
                "\t  [MaNV]          NVARCHAR(20) NOT NULL,\n" +
                "\t  [MaQuay]        NVARCHAR(20) NOT NULL,\n" +
                "\t  [NgayHD]        DATETIME NOT NULL,\n" +
                "\t  [NgayBatDau]    DATETIME,\n" +
                "\t  [GioBatDau]     INT,\n" +
                "\t  [NgayKetThuc]   DATETIME,\n" +
                "\t  [GioKetThuc]    INT,\n" +
                "\t  [CaBan]         INT NOT NULL,\n" +
                "\t  [DaIn]          BIT NOT NULL,\n" +
                "\t  [MaHDGoc]       NVARCHAR(36),\n" +
                "\t  [MaTheKHTT]     NVARCHAR(20),\n" +
                "\t  [TriGiaBan]     NUMERIC (19, 2) NOT NULL,\n" +
                "\t  [TienCK]        NUMERIC (19, 2) NOT NULL,\n" +
                "\t  [ThanhTienBan]  NUMERIC (19, 2),\n" +
                "\t  [TienPhuThu]    NUMERIC (19, 2),\n" +
                "\t  [LoaiHoaDon]    INT NOT NULL,\n" +
                "\t  [TienTraKhach]  NUMERIC (19, 2),\n" +
                "\t  [DaCapNhatTon]  BIT,\n" +
                "\t  [DaVanChuyen]   BIT,\n" +
                "\t  [TrungTamTMThuPhi]   BIT\n" +
                "      );";
        CreateThanhToanHoaDon = "(\n" +
                        "      [HoaDonID]         NVARCHAR(36) NOT NULL,\n" +
                        "      [MaHinhThuc]       NVARCHAR(20) NOT NULL,\n" +
                        "      [MaNhomThanhToan]  NVARCHAR(20) NOT NULL,\n" +
                        "      [MaThe]            NVARCHAR(30) NOT NULL,\n" +
                        "      [ChuThe]           NVARCHAR(255),\n" +
                        "      [ThanhTien]        NUMERIC (19, 2) NOT NULL,\n" +
                        "      [TyGiaNgoaiTe]     NUMERIC (19, 2),\n" +
                        "      [ThanhTienQuiDoi]  NUMERIC (19, 2) NOT NULL,\n" +
                        "      [TLFee]            NUMERIC (19, 2),\n" +
                        "      [DaVanChuyen]      BIT,\n" +
                        "      [STT]              INT NOT NULL,\n" +
                        "      PRIMARY KEY ([HoaDonID], [MaHinhThuc], [STT])\n ); ";
        CreateCTHoaDon = "(\n" +
                "      [HoaDonID]           NVARCHAR(36) NOT NULL,\n" +
                "      [HangHoaID]          NVARCHAR(36) NOT NULL,\n" +
                "      [MaHH]               NVARCHAR(20) NOT NULL,\n" +
                "      [STT]                INT NOT NULL,\n" +
                "      [LoaiHangHoaBan]     INT,\n" +
                "      [TenHH]              NVARCHAR(255) NOT NULL,\n" +
                "      [NgayGioQuet]        DATETIME,\n" +
                "      [MaNccDM]            NVARCHAR(20) NOT NULL,\n" +
                "      [SoLuong]            NUMERIC (19, 3) NOT NULL,\n" +
                "      [DGBan]              NUMERIC (19, 4) NOT NULL,\n" +
                "      [TriGiaBan]          NUMERIC (19, 2) NOT NULL,\n" +
                "      [TLCKGiamGia]        NUMERIC (19, 2) NOT NULL,\n" +
                "      [TienGiamGia]        NUMERIC (19, 2) NOT NULL,\n" +
                "      [TriGiaSauGiamGia]   NUMERIC (19, 2),\n" +
                "      [TLCK_GioVang]       NUMERIC (19, 2) NOT NULL,\n" +
                "      [TienCK_GioVang]     NUMERIC (19, 2) NOT NULL,\n" +
                "      [TriGiaSauGioVang]   NUMERIC (19, 2) NOT NULL,\n" +
                "      [TLCKHD]             NUMERIC (19, 2) NOT NULL,\n" +
                "      [TienCKHD]           NUMERIC (19, 2) NOT NULL,\n" +
                "      [TriGiaSauCKHD]      NUMERIC (19, 2) NOT NULL,\n" +
                "      [TLCK_TheGiamGia]    NUMERIC (19, 2) NOT NULL,\n" +
                "      [TienCK_TheGiamGia]  NUMERIC (19, 2) NOT NULL,\n" +
                "      [ThanhTienBan]       NUMERIC (19, 2) NOT NULL,\n" +
                "      [TienPhuThu]         NUMERIC (19, 2) NOT NULL,\n" +
                "      [VATDauRa]           NUMERIC (19, 2) NOT NULL,\n" +
                "      [DonGiaVonBQ]        NUMERIC (19, 2) NOT NULL,\n" +
                "      [TriGiaVonBQ]        NUMERIC (19, 2) NOT NULL,\n" +
                "      [DonGiaBQ]           NUMERIC (19, 2) NOT NULL,\n" +
                "      [TriGiaBQ]           NUMERIC (19, 2) NOT NULL,\n" +
                "      [DaGhiHDTC]          BIT NOT NULL,\n" +
                "      [DaVanChuyen]        BIT NOT NULL,\n" +
                "      [TriGiaVATDauRa]     NUMERIC (19, 2) NOT NULL,\n" +
                "      [LaHangKhuyenMai]    BIT NULL,\n" +
                "      [DaSuDung]\t\t   BIT NULL,\n" +
                "      [HanSuDung]        DATETIME,\n" +
                "      [Serial]            NVARCHAR(200),\n" +
                "      PRIMARY KEY ([HoaDonID], [HangHoaID], [STT])\n" +
                "      );" ;
    }
    //in_thangNam = 09/2015

    public String CreateThanhToanHoaDon(String in_thangNam) { return   "CREATE TABLE [" + in_thangNam +"] " + CreateThanhToanHoaDon  ;}
    public String CreateHoaDon(String in_thangNam) { return   "CREATE TABLE [" + in_thangNam +"] " + CreateHoaDon  ;}
    public String CreateCTHoaDon(String in_thangNam) { return   "CREATE TABLE [" + in_thangNam +"] " + CreateCTHoaDon  ;}
}
