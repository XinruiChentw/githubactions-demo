import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";

function App() {
    const [data, setData] = useState(null);

    const fetchData = async () => {
        const response = await fetch('/api/helloPerson');
        const result = await response.json();
        setData(result);
    };

    useEffect(() => {
        const callApi = async () => {
            await fetchData();
        }
        callApi();
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <div>
                    <h1>API Response:</h1>
                    <p>{data?.name}</p>
                </div>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
