import { useNavigate } from "react-router-dom";

const ModifyPage = ({tno}) => {
    /* 처리 후 화면이동 */
    const navigate = useNavigate()
    const moveToRead = () => {
        navigate({pathname:'/todo/read/${tno}'})
    }
    const moveToList = () => {
        navigate({pathname:'todo/list'})
    }
    return(
        <div className="text-3xl font-extrabold">
            Todo Modify Page
        </div>

    );
}

export default ModifyPage;