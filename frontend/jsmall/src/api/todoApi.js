import axios from "axios"

const API_SERVER_HOST = 'http://localhost:80'
const prefix = '${API_SERVER_HOST}/api/todo'

/* axios read one */
export const getOne = async(tno) =>{
    const res = await axios.get('${prefix}/${tno}')
    return res.data
}

/* axios read list */
export const getList = async(pageParam) =>{
    const {page,size} = pageParam
    const res = await axios.get('${prefix}/list', {params:{page:page, size:size}})
    return res.data
}