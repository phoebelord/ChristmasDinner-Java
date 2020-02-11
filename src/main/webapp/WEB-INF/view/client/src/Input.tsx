import * as React from "react";

export interface InputProps {
    name?: string
    placeholder?: string
    type: string
    className?: string
    value?: string,
    id?: string
}

function Input(props: InputProps) {

    return (
        <input {...props} />
    )
}

export default Input