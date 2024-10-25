import axios from "axios"
import { API_SERVER_HOST } from "./todoApi"

const rest_api_key = '458eebf2e7678bcb507f4ddb451fdf03' //by 앱 키 > REST API 키
const redirect_uri = 'http://localhost:3000/member/kakao' // 카카오 로그인 Redirect URI
const auth_code_path = 'https://kauth.kakao.com/oauth/authorize' //인가코드 받는 경로
const access_token_uri = 'https://kauth.kakao.com/oauth/token' //토큰 받는 경로


//인가코드 받기
export const getKaKaoLoginLink = ()=>{
    const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`;
    return kakaoURL
}

//Access Token 받기
export const getAccessToken = async (authCode) =>{
    const header = {
        headers:{"Content-type": "application/x-www-form-urlencoded"}
    }
    const params = {
        grant_type : "authorization_code",
        client_id : rest_api_key,
        redirect_uri: redirect_uri, 
        code : authCode
    }

    const res = await axios.post(access_token_uri, params, header)
    const accessToken = res.data.access_token
    return accessToken
}

//API 서버 호출(인가코드 이용)
export const getMemberWithAccessToken = async(accessToken) =>{
    const res = await axios.get(`${API_SERVER_HOST}/api/member/kakao?accessToken=${accessToken}`)
    return res.data
}
