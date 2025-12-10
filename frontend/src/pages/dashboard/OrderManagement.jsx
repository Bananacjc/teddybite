import React, { useEffect, useState } from 'react';
import {
  Box, Checkbox, Heading, Table, Thead, Tbody, Tr, Th, Td, Badge, Button,
  HStack, Input, Select, IconButton, useToast, Spinner, Stack,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  useDisclosure, Text, VStack, Divider,
  AlertDialog, AlertDialogBody, AlertDialogFooter, AlertDialogHeader, AlertDialogContent, AlertDialogOverlay,
  FormControl, FormLabel, NumberInput, NumberInputField, NumberInputStepper, NumberIncrementStepper, NumberDecrementStepper
} from '@chakra-ui/react';
import { SearchIcon, TriangleDownIcon, TriangleUpIcon, CloseIcon, ViewIcon, DeleteIcon, AddIcon, EditIcon } from '@chakra-ui/icons';
import { getAllOrders, deleteOrder, deleteOrders, updateOrder } from '../../api/orders';
import { useNavigate } from 'react-router-dom';

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const toast = useToast();
  const navigate = useNavigate();

  const [selectedOrder, setSelectedOrder] = useState(null);
  const { isOpen, onOpen, onClose } = useDisclosure();

  // Delete Dialog State
  const { isOpen: isDeleteOpen, onOpen: onDeleteOpen, onClose: onDeleteClose } = useDisclosure();
  const [deleteType, setDeleteType] = useState(null); // 'single' or 'batch'
  const [deleteId, setDeleteId] = useState(null);
  const cancelRef = React.useRef();

  // Selection State
  const [selectedIds, setSelectedIds] = useState([]);

  // Edit State
  const { isOpen: isEditOpen, onOpen: onEditOpen, onClose: onEditClose } = useDisclosure();
  const [editingOrder, setEditingOrder] = useState(null);
  const [editFormData, setEditFormData] = useState({
    paymentId: '',
    orderItems: []
  });

  // Search and Sort State
  const [filters, setFilters] = useState({
    orderId: '',
    status: '',
    paymentId: ''
  });
  const [activeSearches, setActiveSearches] = useState({});
  const [sortConfig, setSortConfig] = useState({ key: 'createdAt', direction: 'descending' });

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const data = await getAllOrders();
      setOrders(data);
    } catch (error) {
      toast({
        title: "Error fetching orders",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleOpenAdd = () => {
    navigate('/order');
  };

  const handleDelete = (id) => {
    setDeleteType('single');
    setDeleteId(id);
    onDeleteOpen();
  };

  const handleEdit = (order) => {
    setEditingOrder(order);
    setEditFormData({
      paymentId: order.paymentId,
      orderItems: order.orderItems.map(item => ({ ...item })) // Deep copy items
    });
    onEditOpen();
  };

  const handleUpdateOrder = async () => {
    try {
      const payload = {
        paymentId: editFormData.paymentId,
        orderItems: editFormData.orderItems,
        // Calculate new totals if needed, but backend often recalculates or trusts frontend.
        // For simplicity, we assume unitPrice stays same, we update lineTotal based on new quantity
      };

      // Recalculate line totals
      payload.orderItems.forEach(item => {
        item.lineTotal = parseFloat((item.unitPrice * item.quantity).toFixed(2));
      });

      await updateOrder(editingOrder.orderId, payload);
      toast({ title: "Order updated", status: "success" });
      onEditClose();
      fetchOrders();
    } catch (error) {
      toast({ title: "Update failed", description: error.message, status: "error" });
    }
  };

  const handleEditItemChange = (index, field, value) => {
    const newItems = [...editFormData.orderItems];
    if (field === 'quantity') {
      newItems[index].quantity = parseInt(value) || 0;
    }
    setEditFormData({ ...editFormData, orderItems: newItems });
  };

  const handleRemoveItem = (index) => {
    const newItems = editFormData.orderItems.filter((_, i) => i !== index);
    setEditFormData({ ...editFormData, orderItems: newItems });
  };

  const handleBatchDelete = () => {
    setDeleteType('batch');
    onDeleteOpen();
  };

  const confirmDelete = async () => {
    onDeleteClose();
    try {
      if (deleteType === 'single') {
        await deleteOrder(deleteId);
        toast({ title: "Order deleted", status: "success" });
      } else if (deleteType === 'batch') {
        await deleteOrders(selectedIds);
        toast({ title: "Orders deleted", status: "success" });
        setSelectedIds([]);
      }
      fetchOrders();
    } catch (error) {
      toast({ title: "Delete failed", status: "error" });
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      const allIds = filteredAndSortedOrders.map(o => o.orderId);
      setSelectedIds(allIds);
    } else {
      setSelectedIds([]);
    }
  };

  const handleSelectRow = (id) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(sid => sid !== id));
    } else {
      setSelectedIds([...selectedIds, id]);
    }
  };

  const handleViewOrder = (order) => {
    setSelectedOrder(order);
    onOpen();
  };

  const handleSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const toggleSearch = (columnKey, isOpen) => {
    setActiveSearches(prev => ({ ...prev, [columnKey]: isOpen }));
    if (!isOpen) {
      handleFilterChange(columnKey, '');
    }
  };

  const filteredAndSortedOrders = React.useMemo(() => {
    let processedOrders = [...orders];

    // Apply Filters
    Object.keys(filters).forEach(key => {
      const filterValue = filters[key].toLowerCase();
      if (filterValue) {
        processedOrders = processedOrders.filter(order => {
          let cellValue = order[key];
          if (cellValue) {
            return cellValue.toString().toLowerCase().includes(filterValue);
          }
          return false;
        });
      }
    });

    // Sort
    if (sortConfig.key !== null) {
      processedOrders.sort((a, b) => {
        let valA = a[sortConfig.key];
        let valB = b[sortConfig.key];

        if (valA === null || valA === undefined) valA = '';
        if (valB === null || valB === undefined) valB = '';

        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (valA > valB) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    return processedOrders;
  }, [orders, sortConfig, filters]);

  const getSortIcon = (columnName) => {
    if (sortConfig.key !== columnName) {
      return <TriangleDownIcon ml={1} w={3} h={3} color="gray.300" />;
    }
    return sortConfig.direction === 'ascending' ?
      <TriangleUpIcon ml={1} w={3} h={3} color="brand.600" /> :
      <TriangleDownIcon ml={1} w={3} h={3} color="brand.600" />;
  };

  const HeaderCell = ({ label, columnKey, width }) => {
    const isSearchOpen = activeSearches[columnKey];

    return (
      <Th width={width} verticalAlign="top" py={2}>
        <Box>
          <HStack justify="space-between" width="100%" mb={isSearchOpen ? 2 : 0}>
            <Box
              cursor="pointer"
              onClick={() => handleSort(columnKey)}
              display="flex"
              alignItems="center"
              _hover={{ color: "brand.600" }}
              flex={1}
            >
              {label} {getSortIcon(columnKey)}
            </Box>
            <IconButton
              aria-label="Search"
              icon={isSearchOpen ? <CloseIcon /> : <SearchIcon />}
              size="xs"
              variant="ghost"
              color={isSearchOpen ? "red.400" : "gray.400"}
              _hover={{ color: isSearchOpen ? "red.500" : "brand.500" }}
              onClick={() => toggleSearch(columnKey, !isSearchOpen)}
            />
          </HStack>

          {isSearchOpen && (
            <Input
              size="sm"
              autoFocus
              placeholder={`Search...`}
              value={filters[columnKey] || ''}
              onChange={(e) => handleFilterChange(columnKey, e.target.value)}
              bg="white"
              borderColor="gray.200"
              _focus={{ borderColor: "brand.500", boxShadow: "none" }}
            />
          )}
        </Box>
      </Th>
    );
  };

  return (
    <Box>
      <Stack direction={{ base: 'column', sm: 'row' }} justify="space-between" mb={6} spacing={4}>
        <Heading size="lg" color="brown.900">Order Management</Heading>
        <Stack direction={{ base: 'column', sm: 'row' }} spacing={2}>
          {selectedIds.length > 0 && (
            <Button leftIcon={<DeleteIcon />} colorScheme="red" variant="outline" onClick={handleBatchDelete} w={{ base: 'full', sm: 'auto' }}>
              Delete Selected ({selectedIds.length})
            </Button>
          )}
          <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleOpenAdd} w={{ base: 'full', sm: 'auto' }}>
            Add New Order
          </Button>
        </Stack>
      </Stack>

      <Box bg="white" borderRadius="xl" boxShadow="sm" p={4}>
        <Box overflowX="auto">
          <Table variant="simple">
            <Thead>
              <Tr>
                <Th width="40px" px={2}>
                  <Checkbox
                    isChecked={selectedIds.length === filteredAndSortedOrders.length && filteredAndSortedOrders.length > 0}
                    isIndeterminate={selectedIds.length > 0 && selectedIds.length < filteredAndSortedOrders.length}
                    onChange={handleSelectAll}
                    colorScheme="brand"
                  />
                </Th>
                <HeaderCell label="Order ID" columnKey="orderId" width="150px" />
                <HeaderCell label="Date & Time" columnKey="createdAt" width="200px" />
                <HeaderCell label="Payment ID" columnKey="paymentId" width="150px" />
                <Th>Items Summary</Th>
                <Th isNumeric>Total items</Th>
                <Th>Action</Th>
              </Tr>
            </Thead>
            <Tbody>
              {isLoading ? (
                <Tr>
                  <Td colSpan={6} textAlign="center" py={10}>
                    <Spinner color="brand.500" />
                  </Td>
                </Tr>
              ) : filteredAndSortedOrders.length === 0 ? (
                <Tr>
                  <Td colSpan={6} textAlign="center" py={10} color="gray.500">
                    No orders found.
                  </Td>
                </Tr>
              ) : (
                filteredAndSortedOrders.map((order) => (
                  <Tr key={order.orderId}>
                    <Td px={2}>
                      <Checkbox
                        isChecked={selectedIds.includes(order.orderId)}
                        onChange={() => handleSelectRow(order.orderId)}
                        colorScheme="brand"
                      />
                    </Td>
                    <Td fontWeight="bold" fontSize="sm">{order.orderId}</Td>
                    <Td fontSize="sm">
                      {new Date(order.createdAt).toLocaleString()}
                    </Td>
                    <Td fontSize="sm">{order.paymentId}</Td>
                    <Td>
                      <Text noOfLines={1} fontSize="sm" color="gray.600">
                        {order.orderItems.map(i => `${i.quantity}x ${i.itemName}`).join(', ')}
                      </Text>
                    </Td>
                    <Td isNumeric fontWeight="bold">
                      {/* Calculate total if not available directly, but backend usually sends unitPrice/quantity */}
                      {/* Assuming we might want to sum totals if not provided at root level, but let's assume we just count items for now */}
                      {order.orderItems.reduce((acc, item) => acc + item.quantity, 0)}
                    </Td>
                    <Td>
                      <HStack spacing={2}>
                        <IconButton size="sm" icon={<ViewIcon />} colorScheme="blue" variant="ghost" onClick={() => handleViewOrder(order)} aria-label="View" />
                        <IconButton size="sm" icon={<EditIcon />} colorScheme="orange" variant="ghost" onClick={() => handleEdit(order)} aria-label="Edit" />
                        <IconButton size="sm" icon={<DeleteIcon />} colorScheme="red" variant="ghost" onClick={() => handleDelete(order.orderId)} aria-label="Delete" />
                      </HStack>
                    </Td>
                  </Tr>
                ))
              )}
            </Tbody>
          </Table>
        </Box>
      </Box>

      {/* Order Details Modal */}
      <Modal isOpen={isOpen} onClose={onClose} size="xl" scrollBehavior="inside">
        <ModalOverlay />
        <ModalContent borderRadius="xl">
          <ModalHeader color="brown.900" fontSize="2xl" fontWeight="bold">
            Order Details
            <Badge ml={3} fontSize="md" colorScheme="gray" variant="subtle" borderRadius="md" px={2} py={1}>
              {selectedOrder?.orderId}
            </Badge>
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody py={6}>
            {selectedOrder && (
              <VStack align="stretch" spacing={8}>
                <HStack spacing={10}>
                  <Box>
                    <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" letterSpacing="wider" mb={1}>Date Placed</Text>
                    <Text fontWeight="bold" color="brown.900" fontSize="lg">{new Date(selectedOrder.createdAt).toLocaleString()}</Text>
                  </Box>
                  <Box>
                    <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" letterSpacing="wider" mb={1}>Payment ID</Text>
                    <Text fontWeight="medium" fontFamily="monospace" fontSize="md">{selectedOrder.paymentId}</Text>
                  </Box>
                </HStack>

                <Box>
                  <Heading size="md" mb={4} color="brown.900">Order Items</Heading>
                  <Box borderTop="1px solid" borderBottom="1px solid" borderColor="gray.100" py={2}>
                    <Table variant="unstyled" size="sm">
                      <Thead>
                        <Tr>
                          <Th color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider">Item</Th>
                          <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="60px">Qty</Th>
                          <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="100px">Price</Th>
                          <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="100px">Total</Th>
                        </Tr>
                      </Thead>
                      <Tbody>
                        {selectedOrder.orderItems.map((item, idx) => (
                          <Tr key={idx} borderBottom={idx !== selectedOrder.orderItems.length - 1 ? "1px solid" : "none"} borderColor="gray.50">
                            <Td py={4}>
                              <Text fontWeight="bold" color="brown.900" fontSize="md">{item.itemName}</Text>
                              {item.remarks && item.remarks.length > 0 && (
                                <Text fontSize="sm" color="gray.500" mt={1}>
                                  {item.remarks.join(', ')}
                                </Text>
                              )}
                            </Td>
                            <Td isNumeric py={4} fontSize="md">{item.quantity}</Td>
                            <Td isNumeric py={4} fontSize="md">{item.unitPrice.toFixed(2)}</Td>
                            <Td isNumeric py={4} fontWeight="bold" fontSize="md">{item.lineTotal.toFixed(2)}</Td>
                          </Tr>
                        ))}
                      </Tbody>
                    </Table>
                  </Box>
                </Box>

                {/* Total Summary */}
                <HStack justify="space-between" bg="brand.50" p={6} borderRadius="xl" border="1px dashed" borderColor="brand.200">
                  <Text fontWeight="bold" color="brown.900" fontSize="lg">Grand Total</Text>
                  <Text fontWeight="800" fontSize="2xl" color="brown.900">
                    RM {selectedOrder.orderItems.reduce((acc, item) => acc + item.lineTotal, 0).toFixed(2)}
                  </Text>
                </HStack>

              </VStack>
            )}
          </ModalBody>
          <ModalFooter pb={6}>
            <Button
              onClick={onClose}
              bg="brand.500"
              color="brown.900"
              fontSize="md"
              fontWeight="bold"
              px={6}
              py={5}
              borderRadius="xl"
              _hover={{ bg: 'brand.400' }}
            >
              Close
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Edit Order Modal */}
      <Modal isOpen={isEditOpen} onClose={onEditClose} size="xl" scrollBehavior="inside">
        <ModalOverlay />
        <ModalContent borderRadius="xl">
          <ModalHeader color="brown.900" fontSize="2xl" fontWeight="bold">
            Edit Order
            <Badge ml={3} fontSize="md" colorScheme="gray" variant="subtle" borderRadius="md" px={2} py={1}>
              {editingOrder?.orderId}
            </Badge>
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody py={6}>
            <VStack spacing={8} align="stretch">
              <FormControl>
                <FormLabel fontWeight="bold" color="brown.900">Payment ID</FormLabel>
                <Input
                  size="lg"
                  value={editFormData.paymentId}
                  onChange={(e) => setEditFormData({ ...editFormData, paymentId: e.target.value })}
                  borderColor="gray.200"
                  _focus={{ borderColor: "brand.500", boxShadow: "none" }}
                  borderRadius="md"
                />
              </FormControl>

              <Box>
                <Heading size="md" mb={4} color="brown.900">Order Items</Heading>

                <Box borderTop="1px solid" borderBottom="1px solid" borderColor="gray.100" py={2}>
                  <Table variant="unstyled" size="sm">
                    <Thead>
                      <Tr>
                        <Th color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider">Item</Th>
                        <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="100px">Price (RM)</Th>
                        <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="100px">Qty</Th>
                        <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="100px">Total (RM)</Th>
                        <Th isNumeric color="gray.400" fontSize="xs" textTransform="uppercase" letterSpacing="wider" width="80px">Action</Th>
                      </Tr>
                    </Thead>
                    <Tbody>
                      {editFormData.orderItems.map((item, index) => (
                        <Tr key={index} borderBottom={index !== editFormData.orderItems.length - 1 ? "1px solid" : "none"} borderColor="gray.50">
                          <Td py={4}>
                            <Text fontWeight="bold" color="brown.900" fontSize="md">{item.itemName}</Text>
                            {item.remarks && item.remarks.length > 0 && (
                              <Text fontSize="sm" color="gray.500" mt={1}>
                                {item.remarks.join(', ')}
                              </Text>
                            )}
                          </Td>
                          <Td isNumeric py={4} fontSize="sm" color="gray.600">{item.unitPrice.toFixed(2)}</Td>
                          <Td isNumeric py={4}>
                            <NumberInput
                              size="md"
                              maxW="80px"
                              min={1}
                              value={item.quantity}
                              onChange={(val) => handleEditItemChange(index, 'quantity', val)}
                              borderRadius="md"
                            >
                              <NumberInputField borderRadius="md" />
                              <NumberInputStepper>
                                <NumberIncrementStepper />
                                <NumberDecrementStepper />
                              </NumberInputStepper>
                            </NumberInput>
                          </Td>
                          <Td isNumeric py={4} fontWeight="bold" fontSize="md" color="brown.900">
                            {(item.unitPrice * item.quantity).toFixed(2)}
                          </Td>
                          <Td isNumeric py={4}>
                            <IconButton
                              icon={<DeleteIcon />}
                              size="sm"
                              colorScheme="red"
                              variant="ghost"
                              onClick={() => handleRemoveItem(index)}
                              aria-label="Remove item"
                              _hover={{ bg: 'red.50' }}
                            />
                          </Td>
                        </Tr>
                      ))}
                    </Tbody>
                  </Table>
                </Box>
                {editFormData.orderItems.length === 0 && (
                  <Text color="red.500" fontSize="sm" mt={2} textAlign="center">Order must have at least one item.</Text>
                )}
              </Box>

              {/* Grand Total Summary */}
              <HStack justify="space-between" bg="brand.50" p={6} borderRadius="xl" border="1px dashed" borderColor="brand.200">
                <Text fontWeight="bold" color="brown.900" fontSize="lg">Grand Total</Text>
                <Text fontWeight="800" fontSize="2xl" color="brown.900">
                  RM {editFormData.orderItems.reduce((acc, item) => acc + (item.quantity * item.unitPrice), 0).toFixed(2)}
                </Text>
              </HStack>
            </VStack>
          </ModalBody>
          <ModalFooter pb={6}>
            <Button
              variant="ghost"
              mr={3}
              onClick={onEditClose}
              fontSize="md"
              fontWeight="bold"
              color="brown.900"
              _hover={{ bg: 'transparent', color: 'black' }}
            >
              Cancel
            </Button>
            <Button
              bg="brand.500"
              color="brown.900"
              fontSize="md"
              fontWeight="bold"
              px={6}
              py={5}
              borderRadius="xl"
              _hover={{ bg: 'brand.400' }}
              onClick={handleUpdateOrder}
              isDisabled={editFormData.orderItems.length === 0}
              leftIcon={null}
            >
              Save Changes
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <AlertDialog
        isOpen={isDeleteOpen}
        leastDestructiveRef={cancelRef}
        onClose={onDeleteClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Order{deleteType === 'batch' ? 's' : ''}
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure? This action cannot be undone.
              {deleteType === 'batch' && (
                <Text mt={2} fontWeight="bold">
                  You are about to delete {selectedIds.length} orders.
                </Text>
              )}
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onDeleteClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={confirmDelete} ml={3}>
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </Box>
  );
};

export default OrderManagement;
