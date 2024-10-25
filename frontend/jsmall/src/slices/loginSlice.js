import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import { loginPost } from "../api/MemberApi"
import { getCookie, removeCookie, setCookie } from "../util/cookieUtil"

const initState = {
    email:''
}

/* 쿠키에서 로그인 정보 로딩 */
const loadMemberCookie = () =>{
    const memberInfo = getCookie("member")
    //닉네임 처리
    if(memberInfo && memberInfo.nickname){
        memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }
    return memberInfo
}



/* 비동기 통신 호출 함수 */
export const loginPostAsync = createAsyncThunk('loginPostAsync', (param) =>{
    return loginPost(param)
})

const loginSlice = createSlice({
    name:'LoginSlice',
    initialState: loadMemberCookie() || initState, //쿠키 없으면 초기값 사용
    reducers:{
        login: (state, action) =>{
            console.log("login....")
            //{email, pw}
            //const data = action.payload
            //소셜로그인 회원
            const payload = action.payload
            setCookie("member", JSON.stringify(payload), 1)
            //새로운 상태
            return payload
        },
        logout:(state, action) =>{
            console.log("logout....")
            //쿠키 삭제
            removeCookie("member")
            return {...initState}
        }
    },
    extraReducers:(builder) =>{
        builder.addCase(loginPostAsync.fulfilled, (state, action) =>{
            console.log("fulfilled") //완료
            //로그인 후처리
            const payload = action.payload
            //cookie 생성
            if(!payload.error){
                setCookie("member", JSON.stringify(payload), 1) //1일 = refreshtoken 기한
            }
            return payload
        })
        .addCase(loginPostAsync.pending, (state, action)=>{
            console.log("pending") //처리중
        })
        .addCase(loginPostAsync.rejected, (state, action) => {
            console.log("rejected") //에러
        })
    }
})

export const {login, logout} = loginSlice.actions //loginSlice 내부에 선언된 함수들을 외부에 노출하기 위해
export default loginSlice.reducer