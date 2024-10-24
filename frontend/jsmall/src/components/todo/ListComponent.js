import { useEffect, useState } from "react"
import useCustomMove from "../../hooks/useCustomMove"
import { getList } from "../../api/todoApi"
import PageComponent from "../common/PageComponent"

/* useCustomMove : 현재 경로의 page,size 구성->api 서버 호출 */
const initState = { //PageResponseDTO
    dtoList:[],
    pageNumList:[],
    pageRequestDTO:null,
    prev:false, 
    next:false,
    totalCount:0, 
    prevPage:0, 
    nextPage:0, 
    totalPage:0, 
    current:0
}

const ListComponent = ()=>{
    const {page, size, refresh, moveToList, moveToRead} = useCustomMove() //refresh, moveToList가 추가적으로 필요
    const [serverData, setServerData] = useState(initState) //serverData 나중에 사용
    useEffect(()=>{
        getList({page, size}).then(data=>{
            console.log(data)
            setServerData(data)
        })
    },[page,size,refresh])
    return(
        <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
            <div className="flex flex-wrap mx-auto justify-center p-6">
                {serverData.dtoList.map(todo=>
                <div key={todo.tno} className="w-full min-w-[400px] p-2 m-2 rounded shadow-md" onClick={()=>moveToRead(todo.tno)}>
                    <div className="flex">
                        <div className="font-extrabold text-2xl p-2 mr-4 w-1/12">{todo.tno}</div>
                        <div className="text-1xl m-1 p-2 w-8/12 font-extrabold">{todo.title}</div>
                        <div className="text-1xl m-1 p-2 w-2/10 font-normal">{todo.dueDate}</div>
                    </div>
                </div>
            )}
            </div>
            <PageComponent serverData={serverData} movePage={moveToList}></PageComponent>
        </div>
    );

}

export default ListComponent