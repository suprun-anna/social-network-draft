import React from 'react';


const ConfirmationModal = ({ message, onConfirm, onCancel }) => {
    return (
        <div className="modal">
            <div className="modal-content">
                <p>{message}</p>
                <div className="buttons">
                    <button className='button' onClick={onConfirm}>Yes</button>
                    <button className='button' onClick={onCancel}>No</button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationModal;