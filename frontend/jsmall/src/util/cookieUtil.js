import { Cookies } from "react-cookie";

/* 쿠키의 저장, 조회, 삭제 */
const cookies = new Cookies()

export const setCookie = (name, value, days) =>{
    const expires = new Date()
    expires.setUTCDate(expires.getUTCDate()+days) //보관기한
    return cookies.set(name, value, {path:'/', expires:expires})
}

export const getCookie = (name) =>{
    return cookies.get(name)
}

export const removeCookie = (name, path="/") =>{
    cookies.remove(name, {path})
}