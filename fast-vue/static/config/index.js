/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-25 20:25:32
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-12 01:18:58
 * @FilePath: /common/home/master/project/GuliMall/fast-vue/static/config/index.js
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};

  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:5500/api';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
