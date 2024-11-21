import axios from "axios"
import jwtAxios from "../util/jwtUtil"

export const API_SERVER_HOST = 'http://localhost:80' 
const prefix = `${API_SERVER_HOST}/api/todo`

/* axios read one */
export const getOne = async (tno) =>{
    console.log(prefix)
    const res = await jwtAxios.get(`${prefix}/${tno}`)
    return res.data
}

/* axios read list */
export const getList = async (pageParam) =>{
    const {page,size} = pageParam
    const res = await jwtAxios.get(`${prefix}/list`, {params:{page:page, size:size}})
    return res.data
}

/* axios todo register */
export const postAdd = async (todoObj) =>{
    const res = await jwtAxios.post(`${prefix}/`, todoObj)
    return res.data
}

/* axios todo modify /{tno} */
export const putOne = async (todo) => {
    const res = await jwtAxios.put(`${prefix}/${todo.tno}`, todo)
    return res.data
}

/* axios todo delete /{tno}*/
export const deleteOne = async (tno) =>{
    const res = await jwtAxios.delete(`${prefix}/${tno}`)
    return res.data
}