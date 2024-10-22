import { useCallback } from "react";
import { createSearchParams, useNavigate, useParams, useSearchParams } from "react-router-dom";
import ReadComponent from "../../components/todo/ReadComponent";

const ReadPage = () => {
    const {tno} = useParams()
    const navigate = useNavigate()
    const [queryParams] = useSearchParams()
   /* 화면이동 시 쿼리스트링 유지 */
    const page = queryParams.get("page") ? parseInt(queryParams.get("page")) : 1
    const size = queryParams.get("size") ? parseInt(queryParams.get("size")) : 10   
    const queryStr = createSearchParams({page, size}).toString()
    
    const moveToModify = useCallback((tno)=>{
        navigate({pathname:'/todo/modify/${tno}', search: queryStr})
    },[tno, page, size])

    /* 리스트 페이지로 돌아가기 */
    const moveToList = useCallback(()=>{
        navigate({pathname:'/todo/list', search: queryStr})
    }, [page, size])

    return (
        <div className="text-3xl font-extrabold">
            Todo Read Page Component {tno}
            <div>
                <ReadComponent tno={tno}></ReadComponent>
            </div>
        </div>
    );
}

export default ReadPage;