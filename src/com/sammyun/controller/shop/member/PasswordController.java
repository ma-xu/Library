/*
 * Copyright 2012-2014 sammyun.com.cn. All rights reserved.
 * Support: http://www.sammyun.com.cn
 * License: http://www.sammyun.com.cn/license
 */
package com.sammyun.controller.shop.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sammyun.Setting;
import com.sammyun.controller.shop.BaseController;
import com.sammyun.entity.Member;
import com.sammyun.service.MemberService;
import com.sammyun.util.SettingUtils;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author Sencloud Team

 */
@Controller("shopMemberPasswordController")
@RequestMapping("/member/password")
public class PasswordController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		Member member = memberService.getCurrent();
		if (StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit() {
		return "shop/member/password/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String currentPassword, String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			return ERROR_VIEW;
		}
		if (!isValid(Member.class, "password", password)) {
			return ERROR_VIEW;
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
			return ERROR_VIEW;
		}
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.ct";
	}

}