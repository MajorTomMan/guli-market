/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-04 00:31:52
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-27 23:47:54
 * @FilePath: \Guli\fast-vue\src\components\upload\policy.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import http from '@/utils/httpRequest.js'
export function policy() {
   return  new Promise((resolve,reject)=>{
        http({
            url: http.adornUrl("/third/oss/policy"),
            method: "get",
            params: http.adornParams({})
        }).then(({ data }) => {
            resolve(data);
        })
    });
}
