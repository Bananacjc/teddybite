import React, { useEffect, useState } from 'react';
import { Image, Skeleton, Box } from '@chakra-ui/react';
import apiClient, { baseURL } from '../api/client';

const SecureImage = ({ src, alt, ...props }) => {
    const [imageSrc, setImageSrc] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        // If no source, handle as error/empty
        if (!src) {
            setIsLoading(false);
            return;
        }

        // If it's a standard HTTP/HTTPS url (e.g. placeholder), checks if it needs auth or not
        // For simplicity, if it starts with 'http' and NOT our baseURL, render directly
        if (src.startsWith('http') && !src.includes(baseURL)) {
            setImageSrc(src);
            setIsLoading(false);
            return;
        }

        // If it's a relative path or part of our backend, fetch with headers
        let fetchUrl = src;
        if (!src.startsWith('http')) {
            // Assume it is a relative path like 'uploads/...' or just a filename
            // Our getImageUrl logic in parent might have already prefixed it, 
            // but we want the relative path for the API call if possible, 
            // OR we use the full URL if apiClient supports it.

            // However, apiClient.get() usually expects a relative path from /api
            // Images are served at host/uploads, which is NOT under /api usually?
            // Let's check WebMvcConfig. It serves /uploads/** 
            // So fetching via apiClient (baseURL/api) might be wrong path: baseURL/api/uploads vs baseURL/uploads.

            // We need to fetch from baseURL/uploads/..., but using the headers from apiClient.
            // Let's manually construct the axios call.
        }

        const fetchImage = async () => {
            try {
                // Determine the full URL to fetch
                // We know src might be: 
                // 1. "http://ngrok.../uploads/file.jpg" (from getImageUrl)
                // 2. "/uploads/file.jpg"
                // 3. "file.jpg"

                let targetUrl = src;

                // If it is a full URL, use it.
                // If relative to backend root...

                // We'll use axios directly to allow full URL usage + configured headers
                const response = await apiClient.get(targetUrl, {
                    responseType: 'blob',
                    baseURL: '' // Override baseURL so we can pass full URL or absolute path
                });

                const objectUrl = URL.createObjectURL(response.data);
                setImageSrc(objectUrl);
            } catch (err) {
                console.error("Failed to load secure image:", src, err);
                setError(true);
            } finally {
                setIsLoading(false);
            }
        };

        fetchImage();

        // Cleanup blob on unmount
        return () => {
            if (imageSrc && imageSrc.startsWith('blob:')) {
                URL.revokeObjectURL(imageSrc);
            }
        };
    }, [src]);

    if (isLoading) {
        return <Skeleton w={props.boxSize || props.w || props.width || '100%'} h={props.boxSize || props.h || props.height || '100%'} borderRadius={props.borderRadius} />;
    }

    if (error || !imageSrc) {
        return <Image src="https://via.placeholder.com/150?text=No+Image" alt={alt} {...props} />;
    }

    return <Image src={imageSrc} alt={alt} {...props} />;
};

export default SecureImage;
