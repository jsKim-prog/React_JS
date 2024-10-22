import BasicMenu from "../components/menus/BasicMenu";

//상단 : 공통메뉴, 링크 - 하단: 페이지 컴포넌트 출력
const BasicLayout = ({children}) =>{
    return (
        <>
        {/* 헤더 */}
        <BasicMenu></BasicMenu>

        {/* 상단여백 my-5 제거 */}
        <div className="bg-white my-5 w-full flex flex-col space-y-1 md:flex-row md:space-x-1 md:space-y-0">
            <main className="bg-sky-300 md:w-4/5 lg:w-3/4 px-5 py-5">{children}</main>
            <aside className="bg-green-300 md:w-1/5 lg:w-1/4 px-5 py-5">
            <h1 className="text-2xl md:text-4xl">Sidebar</h1>
            </aside>
        </div>
        </>
    );
}
export default BasicLayout;