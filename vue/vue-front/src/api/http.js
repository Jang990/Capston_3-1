import axios from "axios"
import store from "@/store/store";
const instance = axios.create({
    baseURL: 'http://localhost:8080/api',
});

instance.interceptors.request.use(function(config) {
    // 토큰이 있다면 request header에 넣어서 보내는 인터셉터
    if(store.state.auth.token !== null) {
        config['headers'] = {
            Authorization: `Bearer ${store.state.auth.token}`
        }
    }

    // 토큰이 만료되었다면 재발급 받는 기능도 여기 추가하면 된다.

    return config;
})


export default instance;