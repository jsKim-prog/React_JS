import axios from "axios";
import { getCookie, setCookie } from "./cookieUtil";
import { API_SERVER_HOST } from "../api/todoApi";

/* Axios 요청/응답 시 JWT 처리 */
const jwtAxios = axios.create()

//before request
const beforeReq = (config) =>{
    console.log("before request..")
    const memberInfo = getCookie("member")
    if(!memberInfo){
        console.log("Member NOT FOUND")
        return Promise.reject(
            {response:
                {data : {error:"REQUIRE_LOGIN"}}
            }
        )
    }
    const {accessToken} = memberInfo
    //Authorization 헤더 처리
    config.headers.Authorization = `Bearer ${accessToken}`
    return config
}

//fail request
const requestFail = (err) => {
    console.log("fail request..")
    return Promise.reject(err)
}

//before return response
const beforeRes = async (res) =>{
    console.log("before return response..: "+res)
    const data = res.data
    //ERROR_ACCESS_TOKEN 시 refreshToken 사용, 갱신
    if(data && data.error ==='ERROR_ACCESS_TOKEN'){
        const memberCookieValue = getCookie("member")
        const result = await refresJWT(memberCookieValue.accessToken, memberCookieValue.refreshToken)
        console.log("refresJWT result..: "+result)
        memberCookieValue.accessToken = result.accessToken
        memberCookieValue.refreshToken = result.refreshToken
        setCookie("member", JSON.stringify(memberCookieValue), 1)

        //갱신 토큰으로 원래의 호출 다시 시도
        const originalRequest = res.config
        originalRequest.headers.Authorization = `Bearer ${result.accessToken}`
        return await axios(originalRequest)

    }
    return res
}

//fail response
const responseFail = (err) =>{
    console.log("fail response..")
    return Promise.reject(err)
}

//accessToken 문제시 /api/member/refresh 호출 -> accessToken 갱신(리액트의 refreshToken 사용)
const refresJWT = async (accessToken, refreshToken) =>{
    const host = API_SERVER_HOST
    const header = {headers:{"Authorization":`Bearer ${accessToken}`}}
    const res = await axios.get(`${host}/api/member/refresh?refreshToken=${refreshToken}`,header)
    console.log("refresJWT....:"+res.data)
    return res.data
}


jwtAxios.interceptors.request.use(beforeReq, requestFail)
jwtAxios.interceptors.response.use(beforeRes, responseFail)

export default jwtAxios