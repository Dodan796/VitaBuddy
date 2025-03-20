package com.example.vitabuddy.migration;


import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateImages {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "C##VITABUDDY";
        String password = "1234";

        // 시작 SupID 및 파일 시작 경로
        int startId = 1000;
        int endId = 1200; 
        String basePath = "C:\\supplement_images\\"; // 이미지 파일 경로 (C 드라이브 아래 suppement_images 폴더)
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
        	conn.setAutoCommit(false);
            String sql = "UPDATE supplement SET SupImg = ? WHERE SupID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // SupID에 따라 루프 실행
            for (int supId = startId; supId <= endId; supId++) {
                String imagePath = basePath + supId + ".png"; // 예: "C:\\supplement_images\\1000.png"
                File imageFile = new File(imagePath);
                
                // 이미지 파일이 있는지 확인
                if (imageFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        pstmt.setBinaryStream(1, fis, (int) imageFile.length());
                        pstmt.setInt(2, supId);

                        // 실행 및 커밋
                        int rowsAffected = pstmt.executeUpdate();
                        System.out.println("SupID " + supId + ": Rows affected: " + rowsAffected);
                    }
                } else {
                    System.out.println("SupID " + supId + ": 파일이 없습니다. 경로 확인 -> " + imagePath);
                }
            }
            
            // 전체 커밋
            conn.commit();
            System.out.println("모든 이미지가 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
