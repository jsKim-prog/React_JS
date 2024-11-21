import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";
import jwtAxios from "../util/jwtUtil";

const host = `${API_SERVER_HOST}/api/products`

//상품등록(+이미지파일) :  @PostMapping("/")
export const postAdd = async (product) =>{
    const header = {headers:{"Content-Type":"multipart/form-data"}}
    const res = await jwtAxios.post(`${host}/`, product, header)
    return res.data
}


//상품리스트 : @GetMapping("/list")
export const getList = async (pageParam) =>{
    const {page,size} = pageParam
    const res = await jwtAxios.get(`${host}/list`, {params:{page:page, size:size}})
    return res.data
}

//상품조회 :  @GetMapping("/{pno}")
export const getOne = async (pno) => {
    const res = await jwtAxios.get(`${host}/${pno}`)
    return res.data
}

//상품수정 :  @PutMapping("/{pno}")
export const putOne = async (pno, product) =>{
    const header = {headers:{"Content-Type":"multipart/form-data"}}
    const res = await jwtAxios.put(`${host}/${pno}`, product, header)
    return res.data
}

//상품삭제 처리 :  @DeleteMapping("/{pno}")
export const deleteOne = async (pno) =>{
    const res = await jwtAxios.delete(`${host}/${pno}`)
    return res.data
}