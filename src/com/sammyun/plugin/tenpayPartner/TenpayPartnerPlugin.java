/*
 * Copyright 2012-2014 sammyun.com.cn. All rights reserved.
 * Support: http://www.sammyun.com.cn
 * License: http://www.sammyun.com.cn/license
 */
package com.sammyun.plugin.tenpayPartner;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import com.sammyun.entity.Payment;
import com.sammyun.entity.PluginConfig;
import com.sammyun.plugin.PaymentPlugin;

/**
 * Plugin - 财付通(担保交易)
 * 
 * @author Sencloud Team

 */
@Component("tenpayPartnerPlugin")
public class TenpayPartnerPlugin extends PaymentPlugin
{

    @Override
    public String getName()
    {
        return "财付通(担保交易)";
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }

    @Override
    public String getAuthor()
    {
        return "Sencloud";
    }

    @Override
    public String getSiteUrl()
    {
        return "http://www.sammyun.com.cn";
    }

    @Override
    public String getInstallUrl()
    {
        return "tenpay_partner/install.ct";
    }

    @Override
    public String getUninstallUrl()
    {
        return "tenpay_partner/uninstall.ct";
    }

    @Override
    public String getSettingUrl()
    {
        return "tenpay_partner/setting.ct";
    }

    @Override
    public String getRequestUrl()
    {
        return "https://gw.tenpay.com/gateway/pay.htm";
    }

    @Override
    public RequestMethod getRequestMethod()
    {
        return RequestMethod.get;
    }

    @Override
    public String getRequestCharset()
    {
        return "UTF-8";
    }

    @Override
    public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request)
    {
        PluginConfig pluginConfig = getPluginConfig();
        Payment payment = getPayment(sn);
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("trade_mode", "2");
        parameterMap.put("partner", pluginConfig.getAttribute("partner"));
        parameterMap.put("input_charset", "utf-8");
        parameterMap.put("sign_type", "MD5");
        parameterMap.put("return_url", getNotifyUrl(sn, NotifyMethod.sync));
        parameterMap.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("subject",
                StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 30));
        parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 30));
        parameterMap.put("trans_type", "1");
        parameterMap.put("seller_id", pluginConfig.getAttribute("partner"));
        parameterMap.put("total_fee", payment.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
        parameterMap.put("fee_type", "1");
        parameterMap.put("spbill_create_ip", request.getLocalAddr());
        parameterMap.put("attach", "preschoolEdu");
        parameterMap.put("sign", generateSign(parameterMap));
        return parameterMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request)
    {
        PluginConfig pluginConfig = getPluginConfig();
        if (generateSign(request.getParameterMap()).equals(request.getParameter("sign"))
                && pluginConfig.getAttribute("partner").equals(request.getParameter("partner"))
                && sn.equals(request.getParameter("out_trade_no")) && "0".equals(request.getParameter("trade_state")))
        {
            try
            {
                Map<String, Object> parameterMap = new HashMap<String, Object>();
                parameterMap.put("input_charset", "utf-8");
                parameterMap.put("sign_type", "MD5");
                parameterMap.put("partner", pluginConfig.getAttribute("partner"));
                parameterMap.put("notify_id", request.getParameter("notify_id"));
                String verifyUrl = "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml?input_charset=utf-8&sign_type=MD5&partner="
                        + pluginConfig.getAttribute("partner")
                        + "&notify_id="
                        + request.getParameter("notify_id")
                        + "&sign=" + generateSign(parameterMap);
                Document document = new SAXReader().read(new URL(verifyUrl));
                Node node = document.selectSingleNode("/root/retcode");
                if ("0".equals(node.getText().trim()))
                {
                    return true;
                }
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean verifyMobileNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request)
    {
        if (notifyMethod == NotifyMethod.async)
        {
            return "Success";
        }
        return null;
    }

    @Override
    public Integer getTimeout()
    {
        return 21600;
    }

    /**
     * 生成签名
     * 
     * @param parameterMap 参数
     * @return 签名
     */
    private String generateSign(Map<String, ?> parameterMap)
    {
        PluginConfig pluginConfig = getPluginConfig();
        return DigestUtils.md5Hex(
                joinKeyValue(new TreeMap<String, Object>(parameterMap), null,
                        "&key=" + pluginConfig.getAttribute("key"), "&", true, "sign")).toUpperCase();
    }

}
