import {useLocation} from "react-router";
import React, {useState} from "react";

export function Config() {
    const location = useLocation();
    const [config, setConfig] = useState(location.state.config);

    console.log(config);
    console.log(location.state.config);

    return (
        <p>:)))</p>
    )

}