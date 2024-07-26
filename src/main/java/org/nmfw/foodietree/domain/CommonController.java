package org.nmfw.foodietree.domain;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerIssueDetailDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final CustomerMapper customerMapper;
    private final StoreMapper storeMapper;

    @GetMapping
    public String root(HttpSession session) {
        String loggedInUser = LoginUtil.getLoggedInUser(session);
        if (loggedInUser != null && customerMapper.findOne(loggedInUser) != null) {
            return "redirect:/product/main";
        }
        if (loggedInUser != null && storeMapper.findOne(loggedInUser) != null) {
            return "redirect:/store/mypage/main";
        }
        return "index";
    }
}
