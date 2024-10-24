import axios from "axios"

export const API_SERVER_HOST = 'http://localhost:80' 
const prefix = API_SERVER_HOST+'/api/todo'

/* axios read one */
export const getOne = async (tno) =>{
    console.log(prefix)
    let url = prefix+'/'+tno
    const res = await axios.get(url)
    return res.data
}

/* axios read list */
export const getList = async (pageParam) =>{
    const {page,size} = pageParam
    const res = await axios.get(`${prefix}/list`, {params:{page:page, size:size}})
    return res.data
}

/* axios todo register */
export const postAdd = async (todoObj) =>{
    const res = await axios.post(`${prefix}/`, todoObj)
    return res.data
}

/* axios todo modify /{tno} */
export const putOne = async (todo) => {
    const res = await axios.put(`${prefix}/${todo.tno}`, todo)
    return res.data
}

/* axios todo delete /{tno}*/
export const deleteOne = async (tno) =>{
    const res = await axios.delete(`${prefix}/${tno}`)
    return res.data
}