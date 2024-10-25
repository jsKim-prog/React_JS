import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi";
import { useDispatch } from "react-redux";
import { login } from "../../slices/loginSlice";
import useCustomLogin from "../../hooks/useCustomLogin";

const KakaoRedirectPage = () => {
    const [searchParams] = useSearchParams()
    const authCode = searchParams.get("code")

    /* 로그인 처리(기존 로그인 사용) */
    const dispatch = useDispatch()
    /* 화면이동 */
    const { moveToPath } = useCustomLogin()

    useEffect(() => {
        getAccessToken(authCode).then(accessToken => {
            console.log(accessToken)
            getMemberWithAccessToken(accessToken).then(memberInfo => {
                console.log("memberInfo : " + memberInfo)
                dispatch(login(memberInfo))
                //소셜 회원 아닌 경우
                if(memberInfo && !memberInfo.social){
                    moveToPath("/")
                }else{
                    moveToPath("/member/modify")
                }
            })
        })
    }, [authCode])




    return (
        <div>
            <div>Kakao Login Redirect</div>
            <div>{authCode}</div>
        </div>
    );
}
export default KakaoRedirectPage
