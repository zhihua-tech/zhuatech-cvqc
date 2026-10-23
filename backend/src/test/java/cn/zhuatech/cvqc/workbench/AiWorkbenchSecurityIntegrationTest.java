/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cvqc.workbench;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.zhuatech.cvqc.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@SpringBootTest
@AutoConfigureMockMvc
class AiWorkbenchSecurityIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JwtService jwt;

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsAnonymous() throws Exception {
    mvc.perform(
            post("/api/workbench/inspection-confusion-matrix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"threshold\":0.5,\"samples\":[{\"id\":\"S1\",\"actualDefect\":true,\"defectProbability\":0.9},{\"id\":\"S2\",\"actualDefect\":false,\"defectProbability\":0.7},{\"id\":\"S3\",\"actualDefect\":true,\"defectProbability\":0.1},{\"id\":\"S4\",\"actualDefect\":false,\"defectProbability\":0.1}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsInvalidBearer() throws Exception {
    mvc.perform(
            post("/api/workbench/inspection-confusion-matrix")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"threshold\":0.5,\"samples\":[{\"id\":\"S1\",\"actualDefect\":true,\"defectProbability\":0.9},{\"id\":\"S2\",\"actualDefect\":false,\"defectProbability\":0.7},{\"id\":\"S3\",\"actualDefect\":true,\"defectProbability\":0.1},{\"id\":\"S4\",\"actualDefect\":false,\"defectProbability\":0.1}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerCanCompute() throws Exception {
    mvc.perform(
            post("/api/workbench/inspection-confusion-matrix")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"threshold\":0.5,\"samples\":[{\"id\":\"S1\",\"actualDefect\":true,\"defectProbability\":0.9},{\"id\":\"S2\",\"actualDefect\":false,\"defectProbability\":0.7},{\"id\":\"S3\",\"actualDefect\":true,\"defectProbability\":0.1},{\"id\":\"S4\",\"actualDefect\":false,\"defectProbability\":0.1}]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerGetsValidationError() throws Exception {
    mvc.perform(
            post("/api/workbench/inspection-confusion-matrix")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }
}
